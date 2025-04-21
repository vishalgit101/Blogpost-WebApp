document.addEventListener("DOMContentLoaded", () => {
  const toggleBtn = document.getElementById("darkModeToggle");
  const blogContainer = document.getElementById("blog-posts");
  const loadMoreBtn = document.getElementById("loadMoreBtn");
  const saveToastEl = document.getElementById("saveToast");
  const saveToast = new bootstrap.Toast(saveToastEl);

  async function loadPosts() {
    try {
      const res = await fetch("/api/all-posts");
      const data = await res.json();
      console.log(data);
      renderPosts(data);
    } catch (err) {
      console.error("Failed to load posts", err);
    }
  }

  document.addEventListener("DOMContentLoaded", loadPosts);

  function renderPosts(posts) {
    const container = document.getElementById("blog-container");
    container.innerHTML = ""; // Clear previous content

    posts.forEach((post) => {
      const card = document.createElement("div");
      card.className = "blog-card";

      const imageUrl =
        post.url || "https://via.placeholder.com/400x200?text=No+Image";
      const trimmedSummary = limitWords(post.summary, 25);

      card.innerHTML = `
      <img src="${imageUrl}" alt="Blog Image" />
      <div class="card-content">
        <div class="title">${post.title}</div>
        <div class="summary">${trimmedSummary}</div>
        <div class="meta">
          <span>${post.created.split(" ")[0]}</span>
          <div class="actions">
            <button class="save-btn">Save</button>
            <button class="read-btn">Read More</button>
          </div>
        </div>
      </div>
    `;

      container.appendChild(card);

      // Add event listeners for "Save" and "Read More" buttons
      const saveButton = card.querySelector(".save-btn");
      const readButton = card.querySelector(".read-btn");

      saveButton.addEventListener("click", () => saveBtn(post.id));
      readButton.addEventListener("click", () => readBtn(post.id));
    });
  }

  // Helper to limit word count
  function limitWords(text, maxWords) {
    const words = text.split(" ");
    if (words.length <= maxWords) return text;
    return words.slice(0, maxWords).join(" ") + "...";
  }

  function readBtn(id) {
    window.location.href = `/read-blog.html?postId=${id}`;
  }

  function saveBtn(id) {
    alert("Post: " + id + " saved, successfully!");
  }

  toggleBtn.addEventListener("click", () => {
    const html = document.documentElement;
    const currentTheme = html.getAttribute("data-bs-theme");
    const newTheme = currentTheme === "dark" ? "light" : "dark";
    html.setAttribute("data-bs-theme", newTheme);
    toggleBtn.textContent = newTheme === "dark" ? "☀️" : "🌙";
    updateButtonTheme();
  });

  loadMoreBtn.addEventListener("click", loadPosts);

  // Initial load
  loadPosts();
});
