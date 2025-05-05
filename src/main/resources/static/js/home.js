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

// 获取 CSRF Token
const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

async function getAnime() {
    try {
        const response = await fetch('getAllAnimes', {
            method: 'get',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const animeList = await response.json();

        // 修改 cover 属性并添加 type 属性
        animeList.forEach(anime => {
            anime.type = '动漫'; // 添加 type 属性
            anime.cover = './images/home.png';
        });

        return animeList;
    } catch (error) {
        console.error('获取数据出错:', error);
        return [];
    }
}

// Vue应用初始化
const { createApp, ref, computed } = Vue;
createApp({
    setup() {
        // 获取 anime 数据
        const items = ref([]);

        // 调用 getAnime 方法并更新 items
        getAnime().then(animeList => {
            items.value = animeList;
        });

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