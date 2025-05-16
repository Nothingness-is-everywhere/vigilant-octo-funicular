const ID = document.querySelector("meta[name='ID']").getAttribute("content");
const { createApp } = Vue;

createApp({
    data() {
        return {
            videoUrl: `/getAnimeById/${ID}`, // 视频流URL
            loading: false, // 加载状态
            error: null, // 错误信息
            otherAnime: [
                {
                    id: 1,
                    title: '银河守护者',
                    thumbnail: '/images/home.png'
                }
            ]
        };
    },
    mounted() {
        this.initStars();
        // 初始化时不自动播放，等待用户点击
    },
    methods: {
        // 初始化星星动画（保持不变）
        initStars() {
            const canvas = this.$refs.starsCanvas;
            const ctx = canvas.getContext('2d');
            let stars = [];
            const resize = () => {
                canvas.width = window.innerWidth;
                canvas.height = window.innerHeight;
            };

            class Star {
                constructor() { this.reset(); }
                reset() {
                    this.x = Math.random() * canvas.width;
                    this.y = Math.random() * canvas.height;
                    this.size = Math.random() * 1.5 + 0.5;
                    this.speed = Math.random() * 0.05;
                    this.alpha = Math.random();
                }
                update() {
                    this.alpha += this.speed;
                    if (this.alpha > 1 || this.alpha < 0) this.speed *= -1;
                }
                draw() {
                    ctx.fillStyle = `rgba(255, 255, 255, ${this.alpha})`;
                    ctx.beginPath();
                    ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
                    ctx.fill();
                }
            }

            const animate = () => {
                ctx.clearRect(0, 0, canvas.width, canvas.height);
                stars.forEach(star => { star.update(); star.draw(); });
                requestAnimationFrame(animate);
            };

            window.addEventListener('resize', resize);
            resize();
            const starCount = window.innerWidth < 768 ? 150 : 300;
            stars = Array(starCount).fill().map(() => new Star());
            animate();
        },

        // 播放视频（通过点击<video>标签触发）
        handlePlay() {
            this.loading = true;
            const video = this.$refs.videoRef; // 通过ref获取视频元素

            // 手动触发播放（必须在用户交互事件中调用）
            video.play().then(() => {
                this.loading = false; // 播放开始后隐藏加载提示
            }).catch(error => {
                if (error.name === 'NotAllowedError') {
                    this.error = '请点击播放按钮开始播放';
                } else {
                    this.error = '视频加载失败，请检查网络';
                }
                this.loading = false;
            });
        },

        // 重试加载视频
        reloadVideo() {
            const video = this.$refs.videoRef;
            video.load(); // 重新加载视频源
            this.error = null;
            this.loading = true;
        },

        // 返回首页
        goHome() {
            window.location.href = '/home';
        }
    }
}).mount('#app');