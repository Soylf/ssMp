document.addEventListener('DOMContentLoaded', function () {
  const input = document.getElementById('categoryInput');
  const suggestionsBox = document.getElementById('suggestions');
  let debounceTimeout;

  input.addEventListener('input', () => {
    clearTimeout(debounceTimeout);

    debounceTimeout = setTimeout(() => {
      const query = input.value.trim();

      if (query.length < 1) {
        suggestionsBox.innerHTML = '';
        suggestionsBox.style.display = 'none';
        return;
      }

      fetch(`/download?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(data => {
          suggestionsBox.innerHTML = '';
          if (data.length === 0) {
            suggestionsBox.style.display = 'none';
            return;
          }
          data.forEach(category => {
            const div = document.createElement('div');
            div.textContent = category;
            div.addEventListener('click', () => {
              input.value = category;
              suggestionsBox.innerHTML = '';
              suggestionsBox.style.display = 'none';
            });
            suggestionsBox.appendChild(div);
          });
          suggestionsBox.style.display = 'block';
        })
        .catch(error => {
          console.error('Ошибка загрузки категорий:', error);
          suggestionsBox.style.display = 'none';
        });
    }, 300);
  });
});