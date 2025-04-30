// 获取 CSRF Token
const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
// 初始化函数，页面加载完成后执行
document.addEventListener('DOMContentLoaded', function () {
    // 为添加用户按钮添加点击事件监听器
    const addUserButton = document.querySelector('.add-button');
    addUserButton.addEventListener('click', openAddUserModal);

    // 为编辑按钮添加点击事件监听器
    const editButtons = document.querySelectorAll('.edit-button');
    editButtons.forEach(button => {
        button.addEventListener('click', openEditUserModal);
    });

    // 为关闭模态框的按钮添加点击事件监听器
    const closeModalButtons = document.querySelectorAll('.close');
    closeModalButtons.forEach(button => {
        button.addEventListener('click', closeModal);
    });

    // 为添加用户表单添加提交事件监听器
    const addUserForm = document.getElementById('addUserForm');
    if (addUserForm) {
        addUserForm.addEventListener('submit', addUser);
    }

    // 为编辑用户表单添加提交事件监听器
    const editUserForm = document.getElementById('editUserForm');
    if (editUserForm) {
        editUserForm.addEventListener('submit', editUser);
    }
});

// 打开添加用户模态框
function openAddUserModal() {
    const addUserModal = document.getElementById('addUserModal');
    addUserModal.style.display = 'block';
}

// 打开编辑用户模态框
function openEditUserModal(event) {
    const row = event.target.closest('tr');
    const userId = row.cells[0].textContent;
    const username = row.cells[1].textContent;
    const password = row.cells[2].textContent;

    const editUserModal = document.getElementById('editUserModal');
    const userIdInput = document.getElementById('editUserId');
    const usernameInput = document.getElementById('editUsername');
    const passwordInput = document.getElementById('editPassword');

    userIdInput.value = userId;
    usernameInput.value = username;
    passwordInput.value = password;

    editUserModal.style.display = 'block';
}

// 关闭模态框
function closeModal(event) {
    const modal = event.target.closest('.modal');
    modal.style.display = 'none';
}




// 异步函数，获取数据
async function getUsers() {
    try {
        const response = await fetch('/getUser/0', {
            method: 'get',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return await response.json();
    } catch (error) {
        console.error('获取用户数据出错:', error);
        return [];
    }
}

const { createApp, ref, onMounted } = Vue;

createApp({
    setup() {
        const users = ref(getUsers());

        // 模拟 loadTableData 和 showError 函数
        function loadTableData(response) {
            if (response.ok) {
                // 重新加载用户数据
                getUsers().then(data => {
                    users.value = data;
                });
            }
        }

        function showError(message) {
            console.error('错误信息:', message);
        }

        async function removeUser(user) {
            if (confirm('确定要删除该用户吗？')) {
                try {
                    // 发送DELETE请求，删除指定用户
                    const response = await fetch(`/deleteUser/` + user.userId, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json',
                            [csrfHeader]: csrfToken
                        }
                    });
                    // 处理删除响应
                    await loadTableData(response);
                } catch (error) {
                    // 显示错误信息
                    showError(error.message);
                }
            }
        }

        onMounted(async () => {
            users.value = await getUsers();
        });

        return {
            users,
            removeUser
        };
    }
}).mount('#user-table');
