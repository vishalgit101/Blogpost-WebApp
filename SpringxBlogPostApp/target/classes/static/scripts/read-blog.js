

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
	console.log(response)

    const data = await response.json();
	console.log(data)
	const post = data.post;
	const username = data.username;
    renderPost(post, username);
  } catch (error) {
    console.error("Error loading post:", error);
    const container = document.getElementById("post-container");
    if (container) {
      container.innerHTML = `
        <div class="error-message">
          <h2>Failed to load post</h2>
          <p>Please try again later or contact support if the problem persists.</p>
        </div>
      `;
    }
  }
}

//Render blog post to the page
function renderPost(post, username) {
  const container = document.getElementById("post-container");
  if (!container) {
    console.error("Post container not found");
    return;
  }

  container.innerHTML = `
    <article class="blog-post animate-fade-in">
      <div class="container">
        <div class="blog-header">
          <div class="blog-meta">
            <span class="blog-author">
              <svg width="16" height="16" fill="none" stroke="currentColor">
                <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
              </svg>
              ${username || 'Anonymous'}
            </span>
            <span class="blog-date">
              <svg width="16" height="16" fill="none" stroke="currentColor">
                <circle cx="12" cy="12" r="10"/>
                <path d="M12 8v4l3 3"/>
              </svg>
              ${post.created.split(" ")[0]}
            </span>
            <span class="blog-read-time">
              <svg width="16" height="16" fill="none" stroke="currentColor">
                <circle cx="12" cy="12" r="10"/>
                <path d="M12 6v6l4 2"/>
              </svg>
              ${Math.ceil(post.content.split(' ').length / 200)} min read
            </span>
          </div>
          <h1 class="blog-title">${post.title}</h1>
          <p class="blog-summary">${post.summary || 'No summary available'}</p>
          <div class="blog-image">
            <img src="${post.url}" alt="${post.title}">
          </div>
        </div>
        <div class="blog-content">
          ${post.content}
        </div>
      </div>
    </article>
  `;
  
  // loads the comment section after the post is loaded
  document.querySelector(".comment-section-wrapper").style.display = "block"
}

// Admin functionality
const adminSection = document.querySelector(".admin-section");
const editPostBtn = document.getElementById("edit-btn");
const deletePostBtn = document.getElementById("delete-btn");

// Check user role and display admin controls
const userName = localStorage.getItem("userEmail");
const userRole = localStorage.getItem("userRole");

// Display username and role
const usernameRoleField = document.createElement("span");
usernameRoleField.id = "username-role";
usernameRoleField.style.marginRight = "1rem";
usernameRoleField.style.color = "var(--text-secondary)";

if (userName != null) {
  usernameRoleField.textContent = `${userName} (${userRole})`;
  const navLinks = document.querySelector(".nav-links");

  // Insert before the logout button instead of at the beginning
  const logoutBtn = document.getElementById("logoutBtn");
  if (logoutBtn) {
    navLinks.insertBefore(usernameRoleField, logoutBtn);
  }
}

if (userName != null && userRole == "ADMIN" && adminSection) {
  adminSection.style.display = "block";
} else if (adminSection) {
  adminSection.style.display = "none";
}

// Handle login/logout display
const logoutBtn = document.getElementById("logoutBtn");
const loginBtn = document.getElementById("loginBtn");

if (userName != null && logoutBtn && loginBtn) {
  logoutBtn.style.display = "inline";
  loginBtn.style.display = "none";
}

// Delete post functionality
function deletePost() {
  if (!deletePostBtn) return;
  
  deletePostBtn.addEventListener("click", async (e) => {
    e.preventDefault();
    
    const confirmAction = confirm("Are you sure you want to delete this post?");
    
    if (!confirmAction) {
      return;
    }
    
    try {
      const res = await fetch(`/api/admin/delete?postId=${postId}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json"
        }
      });
      
      if (!res.ok) {
        throw new Error("Failed to delete the post");
      }
      
      window.location.href = "/home2.html";
    } catch (err) {
      console.error("Error deleting post:", err);
    }
  });
}

//Load post
if (postId) {
  fetchPost(postId);
  deletePost();
} 


