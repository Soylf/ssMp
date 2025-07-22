document.addEventListener('DOMContentLoaded', function () {
    async function loadCategories() {
        try {
            const response = await fetch('/search/categories');
            const categories = await response.json();
            const grid = document.getElementById('category-grid');
            categories.forEach(category => {
                const div = document.createElement('div');
                div.textContent = category;
                div.addEventListener('click', () => {
                    localStorage.setItem('category', category);
                });
                grid.appendChild(div);
            });
        } catch (e) {
            console.error('Ошибка при загрузке категорий:', e);
        }
    }
    loadCategories();
});
