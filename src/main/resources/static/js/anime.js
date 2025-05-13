const ID = document.querySelector("meta[name='ID']").getAttribute("content");
    // 引入Vue
    const { createApp } = Vue;

    // 创建Vue实例
    createApp({
    // 定义数据
    data() {
    return {
    // 其他动漫信息
    otherAnime: [
{
    // 动漫ID
    id: 1,
    // 动漫标题
    title: '银河守护者',
    // 动漫集数
    episodes: '24',
    // 动漫缩略图
    thumbnail: 'https://picsum.photos/id/237/400/600'
}
    ]
}
},
    // 组件挂载后执行
    mounted() {
    // 初始化星星
    this.initStars();
},
    // 定义方法
    methods: {
    // 初始化星星
    initStars() {
    // 获取canvas元素
    const canvas = this.$refs.starsCanvas;
    // 获取canvas上下文
    const ctx = canvas.getContext('2d');
    // 定义星星数组
    let stars = [];

    // 监听窗口大小变化
    const resize = () => {
    // 设置canvas宽高
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
}

    // 定义星星类
    class Star {
    // 构造函数
    constructor() {
    // 重置星星位置
    this.reset();
}

    // 重置星星位置
    reset() {
    // 随机生成星星位置
    this.x = Math.random() * canvas.width;
    this.y = Math.random() * canvas.height;
    // 随机生成星星大小
    this.size = Math.random() * 1.5 + 0.5;
    // 随机生成星星速度
    this.speed = Math.random() * 0.05;
    // 随机生成星星透明度
    this.alpha = Math.random();
}

    // 更新星星位置
    update() {
    // 更新星星透明度
    this.alpha += this.speed;
    // 如果透明度超过1或小于0，则反转速度
    if(this.alpha > 1 || this.alpha < 0) this.speed *= -1;
}

    // 绘制星星
    draw() {
    // 设置填充颜色
    ctx.fillStyle = `rgba(255, 255, 255, ${this.alpha})`;
    // 开始绘制路径
    ctx.beginPath();
    // 绘制圆形
    ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
    // 填充颜色
    ctx.fill();
}
}

    // 动画函数
    function animate() {
    // 清空画布
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // 遍历星星数组，更新并绘制星星
    stars.forEach(star => {
    star.update();
    star.draw();
});

    // 请求下一帧动画
    requestAnimationFrame(animate);
}

    // 监听窗口大小变化事件
    window.addEventListener('resize', resize);
    // 初始化窗口大小
    resize();

    // 根据屏幕尺寸动态调整星星数量
    const starCount = window.innerWidth < 768 ? 150 : 300;
    // 创建星星数组
    stars = Array(starCount).fill().map(() => new Star());
    // 开始动画
    animate();
},
    // 播放视频
    playVideo() {
    // 这里可以添加视频播放逻辑
    alert('开始播放 ' + this.anime.title + ' - ' + this.currentEpisode);
},
    // 返回首页
    goHome() {
        // 跳转到首页
        window.location.href = '/home'
}
}
}).mount('#app');