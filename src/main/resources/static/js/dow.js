document.addEventListener('DOMContentLoaded', function () {
  const fileInput = document.getElementById('fileInput');
  const fileName = document.getElementById('fileName');
  const message = document.getElementById('message');
  const uploadButton = document.getElementById('uploadButton');

  uploadButton.addEventListener('click', async function () {
    if (!fileInput.files.length) {
      message.textContent = "⚠️ Пожалуйста, выберите файл.";
      return;
    }

    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch('/download', {
        method: 'POST',
        body: formData
      });

      if (response.ok) {
        const text = await response.text();
        message.textContent = "✅ " + text;
      } else {
        message.textContent = "❌ Ошибка при загрузке файла.";
      }
    } catch (error) {
      message.textContent = "⚠️ Ошибка соединения с сервером.";
    }
  });

  fileInput.addEventListener('change', () => {
    fileName.textContent = fileInput.files.length > 0 ? fileInput.files[0].name : "Файл не выбран";
  });
});