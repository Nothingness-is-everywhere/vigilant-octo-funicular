// 动态粒子生成器
function createParticles() {
    const container = document.querySelector('.particles-container');
    const particleCount = 120; // 增加粒子数量提升氛围感

    for (let i = 0; i < particleCount; i++) {
        const particle = document.createElement('div');
        particle.className = 'particle';

        // 随机初始位置和大小
        particle.style.width = `${Math.random() * 4 + 8}px`;
        particle.style.height = particle.style.width;
        particle.style.left = `${Math.random() * 100}vw`;
        particle.style.top = `${Math.random() * 100}vh`;

        // 随机动画速度和透明度
        particle.style.animationDuration = `${Math.random() * 10 + 10}s`;
        particle.style.opacity = `${Math.random() * 0.5 + 0.3}`;

        container.appendChild(particle);
    }
}

// Vue应用初始化
const { createApp, ref, computed } = Vue;
createApp({
    setup() {
        // 模拟数据
        const items = ref([
            { id: 1, title: '鬼灭之刃', author: '吾峠呼世晴', type: '漫画', cover: 'https://i.imgur.com/7XbXJj1.jpg' },
            { id: 2, title: '进击的巨人', author: '谏山创', type: '动漫', cover: 'https://i.imgur.com/5tQZbLm.jpg' },
            { id: 3, title: '咒术回战', author: '芥见下下', type: '漫画', cover: 'https://i.imgur.com/3Z7zQzL.jpg' },
            { id: 4, title: '海贼王', author: '尾田荣一郎', type: '漫画', cover: 'https://i.imgur.com/6zJzXzJ.jpg' },
        ]);

        const searchKeyword = ref('');
        const categories = ref(['全部', '漫画', '动漫']);
        const currentCategory = ref('全部');

        const filteredItems = computed(() => {
            return items.value.filter(item => {
                const categoryMatch = currentCategory.value === '全部' || item.type === currentCategory.value;
                const searchMatch = item.title.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
                    item.author.toLowerCase().includes(searchKeyword.value.toLowerCase());
                return categoryMatch && searchMatch;
            });
        });

        // 页面加载后生成粒子
        window.addEventListener('load', createParticles);

        return {
            searchKeyword,
            categories,
            currentCategory,
            filteredItems
        };
    }
}).mount('#app');