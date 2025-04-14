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
    fetch('/addUser', {
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
                // 刷新页面或更新表格
                location.reload();
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
    fetch('/editUser', {
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
                location.reload();
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
                    await handleDeleteResponse(response, userId);
                } catch (error) {
                    // 显示错误信息
                    showError(error.message);
                }
            }
        });
    });
});

// 异步函数，处理删除响应
async function handleDeleteResponse(response, userId) {
    // 将响应转换为JSON格式
    const data = await response.json();
    // 如果响应成功且数据成功
    if (response.ok && data.success) {
        // 删除成功，从表格中移除该行
        const rowToDelete = document.querySelector(`tr[data-user-id="${userId}"]`);
        // 如果有要删除的行
        if (rowToDelete) {
            // 删除该行
            rowToDelete.remove();
        }
        // 弹出提示框，提示用户删除成功
        alert('用户删除成功！');
        // 重新加载表格
        loadTableData();
    } else {
        // 弹出提示框，提示用户删除失败，并显示失败原因
        alert('删除用户失败：' + data.message);
    }
}

function showError(message) {
    alert('请求出错，请稍后重试！' + message);
}