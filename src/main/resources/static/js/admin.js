// 获取 CSRF Token
const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
const { createApp, ref, onMounted } = Vue;

// 异步函数，获取数据
async function getUsers() {
    try {
        const response = await fetch('/getUserById/0', {
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

// 用户操作
createApp({
    setup() {
        const users = ref([]);
        const isAddUserModalVisible = ref(false);
        const isEditUserModalVisible = ref(false);
        const addUsername = ref('');
        const addPassword = ref('');
        const editUserId = ref('');
        const editUsername = ref('');
        const editPassword = ref('');

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

        function openAddUserModal() {
            isAddUserModalVisible.value = true;
        }

        function openEditUserModal(user) {
            editUserId.value = user.userId;
            editUsername.value = user.username;
            isEditUserModalVisible.value = true;
        }

        function closeModal(type) {
            if (type === 'addUser') {
                isAddUserModalVisible.value = false;
                addUsername.value = '';
                addPassword.value = '';
            } else if (type === 'editUser') {
                isEditUserModalVisible.value = false;
                editUserId.value = '';
                editUsername.value = '';
                editPassword.value = '';
            }
        }

        async function addUser() {
            try {
                const response = await fetch('/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    },
                    body: JSON.stringify({
                        username: addUsername.value,
                        password: addPassword.value
                    })
                });
                await loadTableData(response);
                closeModal('addUser');
            } catch (error) {
                showError(error.message);
            }
        }

        async function editUser() {
            try {
                const response = await fetch(`/editUser/${editUserId.value}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    },
                    body: JSON.stringify({
                        username: editUsername.value,
                        password: editPassword.value
                    })
                });
                await loadTableData(response);
                closeModal('editUser');
            } catch (error) {
                showError(error.message);
            }
        }

        onMounted(async () => {
            users.value = await getUsers();
        });

        return {
            users,
            removeUser,
            openAddUserModal,
            openEditUserModal,
            closeModal,
            addUser,
            editUser,
            isAddUserModalVisible,
            isEditUserModalVisible,
            addUsername,
            addPassword,
            editUserId,
            editUsername,
            editPassword
        };
    }
}).mount('#user');
