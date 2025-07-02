document.addEventListener('DOMContentLoaded', function () {
  async function uploadFile() {
    const fileInput = document.getElementById('fileInput');
    const message = document.getElementById('message');
    const description = document.getElementById('description').value;
    const category = document.getElementById('categoryInput').value;

    if (!fileInput.files.length) {
      message.textContent = "⚠️ Пожалуйста, выберите файл.";
      return;
    }

    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append("file", file);
    formData.append("description", description);
    formData.append("category", category);

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
  }

  const fileInput = document.getElementById('fileInput');
  const fileName = document.getElementById('fileName');
  const uploadButton = document.getElementById('uploadButton');

  fileInput.addEventListener('change', () => {
    fileName.textContent = fileInput.files.length > 0 ? fileInput.files[0].name : "Файл не выбран";
  });

  uploadButton.addEventListener('click', uploadFile);
});