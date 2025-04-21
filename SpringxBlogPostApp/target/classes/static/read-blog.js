//script.js

//Toggle dark mode
document.getElementById("toggleTheme").addEventListener("click", () => {
  document.body.classList.toggle("dark-mode");
});

//Extract postId from query string
const params = new URLSearchParams(window.location.search);
const postId = params.get("postId");

//Fetch blog post data
async function fetchPost(postId) {
  try {
    const response = await fetch(`/api/read?id=${postId}`);
    if (!response.ok) {
      throw new Error("Failed to fetch post");
    }
    const post = await response.json();
    renderPost(post);
  } catch (error) {
    console.error("Error loading post:", error);
    document.getElementById("post-container").innerText =
      "Failed to load post.";
  }
}

//Render blog post to the page
function renderPost(post) {
  const container = document.getElementById("post-container");
  container.innerHTML = `
<img src="${post.url}" alt="Post Image" class="preview-image">
<h1 class="title">${post.title}</h1>
<p class="summary">${post.created.split(" ")[0]}</p>
<hr>
<div class="content">${post.content}</div>
<div class="comment-box">
<h4>Leave a Comment</h4>
<form id="comment-form">
 <div class="mb-3">
   <textarea class="form-control" id="commentText" rows="3" placeholder="Write your comment here..."></textarea>
 </div>
 <button type="submit" class="btn btn-primary">Submit Comment</button>
</form>
<div id="comments" class="mt-4">
 <h5>Comments</h5>
 <div class="comment">Nice blog post! <span class="reply">Reply</span></div>
 <div class="comment">Thanks for sharing! <span class="reply">Reply</span></div>
</div>
</div>
`;
}

//Handle comment submission
document.addEventListener("submit", function (e) {
  if (e.target.id === "comment-form") {
    e.preventDefault();
    const commentText = document.getElementById("commentText").value.trim();
    if (commentText) {
      const commentDiv = document.createElement("div");
      commentDiv.classList.add("comment");
      commentDiv.innerHTML = `${commentText} <span class="reply">Reply</span>`;
      document.getElementById("comments").appendChild(commentDiv);
      document.getElementById("commentText").value = "";
    }
  }
});

//Delegate reply handler
document.addEventListener("click", function (e) {
  if (e.target.classList.contains("reply")) {
    const replyBox = document.createElement("div");
    replyBox.classList.add("reply-box");
    replyBox.innerHTML = `
<textarea class="form-control mb-2" rows="2" placeholder="Write your reply..."></textarea>
<button class="btn btn-sm btn-secondary">Submit Reply</button>
`;
    e.target.after(replyBox);
    e.target.remove();
  }
});

//Load post
if (postId) fetchPost(postId);
