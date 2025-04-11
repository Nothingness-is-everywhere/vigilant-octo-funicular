const signInBtn = document.getElementById("signIn");
const signUpBtn = document.getElementById("signUp");
const loginForm = document.getElementById("form2");  // 修改更合理的变量名
const registerForm = document.getElementById("form1"); // 修改更合理的变量名
const container = document.querySelector(".container");

// 获取 CSRF Token
const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

// 面板切换逻辑保持不变
signInBtn.addEventListener("click", () => container.classList.remove("right-panel-active"));
signUpBtn.addEventListener("click", () => container.classList.add("right-panel-active"));

// 登录表单提交
loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const formData = new URLSearchParams();
    formData.append('username', document.getElementById('username2').value);
    formData.append('password', document.getElementById('password3').value);

    try {
        const response = await fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                [csrfHeader]: csrfToken
            },
            body: formData
        });

        await handleAuthResponse(response);
    } catch (error) {
        showError(error.message);
    }
});

// 注册表单提交
registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const username = document.getElementById('username1').value;
    const password1 = document.getElementById('password1').value;
    const password2 = document.getElementById('password2').value;

    if (!validateRegistration(username, password1, password2)) return;

    try {
        const response = await fetch('/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ username, password: password1 })
        });

        await handleRegistrationResponse(response);
    } catch (error) {
        showError(error.message);
    }
});

// 公共方法
const showError = (message) => {
    alert(`操作失败: ${message}`);
};

const showSuccess = (message) => {
    alert(`成功: ${message}`);
    container.classList.remove("right-panel-active");
};

// 登录响应处理
const handleAuthResponse = async (response) => {
    if (response.redirected) {
        window.location.href = response.url;
        return;
    }

    try {
        const data = await response.json(); // 解析 JSON 数据
        if (!response.ok) {
            throw new Error(data.error || '认证失败');
        }
        showSuccess('登录成功');
    } catch (error) {
        showError(error.message);
    }
};

// 注册验证
const validateRegistration = (username, pwd1, pwd2) => {
    if (!username || !pwd1 || !pwd2) {
        showError('所有字段必须填写');
        return false;
    }
    if (pwd1 !== pwd2) {
        showError('两次密码输入不一致');
        return false;
    }
    return true;
};

// 注册响应处理
const handleRegistrationResponse = async (response) => {
    const data = await response.text();
    if (!response.ok) {
        throw new Error(data || '注册失败');
    }
    showSuccess('注册成功，请登录');
};