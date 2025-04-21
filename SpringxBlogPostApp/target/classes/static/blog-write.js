const quill = new Quill("#editor", {
  modules: {
    toolbar: "#editor-toolbar",
    "emoji-toolbar": true,
    "emoji-textarea": false,
    "emoji-shortname": true,
    imageResize: {},
  },
  placeholder: "Write your awesome blog content here...",
  theme: "snow",
  formats: [
    "header",
    "font",
    "size",
    "bold",
    "italic",
    "underline",
    "strike",
    "color",
    "background",
    "align",
    "list",
    "bullet",
    "image",
    "emoji",
  ],
});

// 👇 Add this immediately after Quill is initialized
const toolbar = quill.getModule("toolbar");
toolbar.addHandler("image", () => {
  const url = prompt("Enter the image URL:");
  if (url) {
    const range = quill.getSelection();
    quill.insertEmbed(range.index, "image", url, Quill.sources.USER);
  }
});

// Word count
const wordCountEl = document.getElementById("word-count");
quill.on("text-change", () => {
  const text = quill.getText().trim();
  const words = text.length > 0 ? text.split(/\s+/).length : 0;
  wordCountEl.innerText = `${words} word${words === 1 ? "" : "s"}`;
});

// Form submit
document.getElementById("blog-form").addEventListener("submit", (e) => {
  e.preventDefault();
  const title = document.getElementById("title").value;
  const summary = document.getElementById("summary").value;
  const content = quill.root.innerHTML;
  const previewImageUrl = document.getElementById("previewImageUrlInput").value;
  console.log({ title, summary, content, previewImageUrl });
});

// Toggle dark mode
document.getElementById("toggleTheme").addEventListener("click", () => {
  document.body.classList.toggle("dark-mode");
  document.querySelector(".container").classList.toggle("dark-mode");
});

// Preview image handler
// Preview image from URL input
document
  .getElementById("previewImageUrlInput")
  .addEventListener("input", (e) => {
    const url = e.target.value;
    const previewImg = document.getElementById("previewImage");
    if (url) {
      previewImg.src = url;
      previewImg.style.display = "block";
    } else {
      previewImg.style.display = "none";
    }
  });

async function sendData(event) {
  event.preventDefault();

  // get the fields data....
  const title = document.getElementById("title").value;
  const summary = document.getElementById("summary").value;
  const img = document.getElementById("previewImageUrlInput").value;
  const content = quill.root.innerHTML; // Grab blog content from Quill editor...
  const plainText = quill.getText().trim();
  // await fetch...

  if (!plainText || plainText === "") {
    alert("Please write something in the blog content.");
    return;
  } else if (wordCountEl < 50) {
    alert("Word Count cant be less than 50.");
    return;
  }

  // Confirmation before publishing
  const isConfirmed = window.confirm(
    "Are you sure you want to publish this blog?"
  );

  if (!isConfirmed) {
    return;
  }

  try {
    const response = await fetch("/api/save", {
      method: "Post",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ title, summary, img, content }),
    });

    if (response.ok) {
      alert("Blog Successfully Published!");
      window.location.href = "/home.html";
    } else {
      throw new Error("Blog failed to get published!");
    }
  } catch (err) {
    console.error("Error: " + err);
  }
}

// document.addEventListener("DOMContentLoaded", () => {
// load();
//document.getElementById("addTaskBtn").addEventListener("click", addTask);
//})

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("publish").addEventListener("click", sendData);
});
