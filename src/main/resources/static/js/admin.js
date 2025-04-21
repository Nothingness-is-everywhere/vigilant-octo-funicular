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

// 添加用户
function addUser(event) {
    event.preventDefault();
    const username = document.getElementById('addUsername').value;
    const password = document.getElementById('addPassword').value;

    // 这里可以使用 fetch 或其他方式发送请求到服务器
    fetch('/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                loadTableData(data)
            } else {
                alert('添加用户失败: ' + data.message);
            }
        })
        .catch(error => {
            alert('添加用户时发生错误: ' + error.message);
        });
}

// 编辑用户
function editUser(event) {
    event.preventDefault();
    const userId = document.getElementById('editUserId').value;
    const username = document.getElementById('editUsername').value;
    const password = document.getElementById('editPassword').value;

    // 这里可以使用 fetch 或其他方式发送请求到服务器
    fetch('/editUser/0', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: userId,
            username: username,
            password: password
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 刷新页面或更新表格
                loadTableData(data);
            } else {
                alert('编辑用户失败: ' + data.message);
            }
        })
        .catch(error => {
            alert('编辑用户时发生错误: ' + error.message);
        });
}

document.addEventListener('DOMContentLoaded', async () => {
    // 为删除按钮添加点击事件监听器
    const deleteButtons = document.querySelectorAll('.delete-button');
    deleteButtons.forEach(button => {
        button.addEventListener('click', async () => {
            const userId = button.dataset.userId;
            if (confirm('确定要删除该用户吗？')) {
                try {
                    // 发送DELETE请求，删除指定用户
                    const response = await fetch(`/deleteUser/${userId}`, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json',
                            [csrfHeader]: csrfToken
                        }
                    });
                    // 处理删除响应
                    await  loadTableData(response);
                } catch (error) {
                    // 显示错误信息
                    showError(error.message);
                }
            }
        });
    });
});

// 异步函数，更新表格
async function  loadTableData(response) {
    // 将响应转换为JSON格式
    const data = await response.json();
    // 如果响应成功且数据成功
    if (response.ok && data.success) {
        // 创建一个XMLHttpRequest对象
        const xhr = new XMLHttpRequest();

        // 配置请求
        xhr.open('GET', '/getUser/0', true);

        // 设置请求完成后的回调函数
        xhr.onload = function() {
            if (xhr.status >= 200 && xhr.status < 300) {
                // 请求成功，解析响应数据
                const data = JSON.parse(xhr.responseText);

                // 获取表格的tbody元素
                const tbody = document.querySelector('#user-table tbody');

                // 清空tbody的内容
                tbody.innerHTML = '';
                // 创建新的行并添加到tbody
                data.forEach(function(user) {
                    const tr = document.createElement('tr');

                    const tdUserId = document.createElement('td');
                    tdUserId.textContent = user.userId;
                    tr.appendChild(tdUserId);

                    const tdUsername = document.createElement('td');
                    tdUsername.textContent = user.username;
                    tr.appendChild(tdUsername);

                    const tdPassword = document.createElement('td');
                    tdPassword.textContent = user.password;
                    tr.appendChild(tdPassword);

                    const tdActions = document.createElement('td');
                    const editButton = document.createElement('button');
                    editButton.className = 'edit-button';
                    editButton.textContent = '编辑';
                    editButton.addEventListener('click', openEditUserModal);
                    tdActions.appendChild(editButton);

                    const deleteButton = document.createElement('button');
                    deleteButton.className = 'delete-button';
                    deleteButton.setAttribute('data-user-id', user.userId);
                    deleteButton.textContent = '删除';
                    deleteButton.addEventListener('click', function() {
                        deleteUser(user.userId);
                    });
                    tdActions.appendChild(deleteButton);

                    tr.appendChild(tdActions);

                    tbody.appendChild(tr);
                });
            } else {
                // 请求失败，处理错误
                console.error('请求失败:', xhr.statusText);
            }
        };
        // 发送请求
        xhr.send();
    } else {
        // 弹出提示框，提示用户删除失败，并显示失败原因
        alert('失败：' + data.message);
    }
}

function showError(message) {
    alert('请求出错，请稍后重试！' + message);
}