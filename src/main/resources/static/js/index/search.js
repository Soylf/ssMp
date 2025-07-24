document.addEventListener('DOMContentLoaded', () => {
  const searchInput = document.getElementById('description');
  const overlay = document.querySelector('.overlay');
  const container = document.querySelector('.results-container');
  const pyramid = document.querySelector('.item-pyramid');

  const modalOverlay = document.getElementById('modal-player-overlay');
  const modalPlayer = document.getElementById('modal-player');

  let activeCard = null;

  searchInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') {
      const msg = searchInput.value.trim();
      if (msg === '') return;
      const category = localStorage.getItem('category') || '';
      fetchItems(msg, category);
    }
  });

  overlay.addEventListener('click', (e) => {
    if (!container.contains(e.target)) {
      hideOverlay();
    }
  });

  modalOverlay.addEventListener('click', (e) => {
    if (e.target === modalOverlay) {
      closeModal();
    }
  });

  function openModal(mediaElement, card) {
    modalPlayer.innerHTML = '';

    const closeBtn = document.createElement('button');
    closeBtn.classList.add('close-btn');
    closeBtn.innerHTML = '&times;';
    closeBtn.addEventListener('click', closeModal);
    modalPlayer.appendChild(closeBtn);

    const clonedMedia = mediaElement.cloneNode(true);
    clonedMedia.controls = true;
    clonedMedia.autoplay = true;
    clonedMedia.style.width = '100%';
    clonedMedia.style.height = 'auto';

    modalPlayer.appendChild(clonedMedia);
    modalOverlay.classList.add('active');

    setActiveCard(card);
  }

  function closeModal() {
    const media = modalPlayer.querySelector('video, audio');
    if (media) {
      media.pause();
      media.src = '';
    }
    modalOverlay.classList.remove('active');

    setTimeout(() => {
      modalPlayer.innerHTML = '';
    }, 300);

    clearActiveCard();
  }

  function setActiveCard(card) {
    if (activeCard) activeCard.classList.remove('active');
    activeCard = card;
    if (activeCard) activeCard.classList.add('active');
  }

  function clearActiveCard() {
    if (activeCard) {
      activeCard.classList.remove('active');
      activeCard = null;
    }
  }

  function hideOverlay() {
    overlay.classList.add('hidden');
    pyramid.innerHTML = '';
    clearActiveCard();
  }

  async function fetchItems(msg, category) {
    try {
      const response = await fetch(`/search?msg=${encodeURIComponent(msg)}&category=${encodeURIComponent(category)}`);
      if (!response.ok) throw new Error('Сетевая ошибка');
      const items = await response.json();

      pyramid.innerHTML = '';
      const areas = ['a', 'b', 'c', 'd', 'e', 'f'];

      items.slice(0, 6).forEach((item, index) => {
        const card = document.createElement('div');
        card.classList.add('item-card');
        if (areas[index]) {
          card.style.gridArea = areas[index];
        }

        const fileName = item.fileLink.split(/[/\\]/).pop().toLowerCase();
        let media;

        if (fileName.endsWith('.mp3')) {
          media = document.createElement('audio');
          media.controls = false;
          media.src = `/files/${fileName}`;
          card.appendChild(media);
        } else if (fileName.endsWith('.mp4')) {
          media = document.createElement('video');
          media.controls = false;
          media.src = `/files/${fileName}`;
          card.appendChild(media);
        } else {
          const notSupported = document.createElement('div');
          notSupported.textContent = 'Файл не поддерживается';
          card.appendChild(notSupported);
        }

        const description = document.createElement('div');
        description.textContent = item.description || 'Без описания';
        description.style.marginTop = '5px';
        description.style.fontWeight = '600';
        description.style.fontSize = '13px';
        card.appendChild(description);

        if (media) {
          card.addEventListener('click', () => openModal(media, card));
        }

        pyramid.appendChild(card);
      });

      overlay.classList.remove('hidden');
    } catch (error) {
      console.error('Ошибка при получении файлов:', error);
      alert('Не удалось загрузить файлы, попробуйте позже.');
    }
  }
});