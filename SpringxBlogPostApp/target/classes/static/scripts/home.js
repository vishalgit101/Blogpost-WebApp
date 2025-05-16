document.addEventListener("DOMContentLoaded", () => {
  const loadMoreBtn = document.getElementById("loadMoreBtn");
  //const saveToast = new bootstrap.Toast(saveToastEl);
  const loginBtn = document.getElementById("loginBtn");
  const logoutBtn = document.getElementById("logoutBtn");

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

  /*function renderPosts(posts) {
    const container = document.getElementById("blog-container");
    container.innerHTML = ""; // Clear previous content

    posts.forEach((post) => {
      const card = document.createElement("div");
      card.className = "blog-card";

      const imageUrl = post.url;
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

      saveButton.addEventListener("click", () => saveBtn(post.id)); // post.id and user.id
      readButton.addEventListener("click", () => readBtn(post.id));
    });
  }*/
  
  function renderPosts(posts) {
    const container = document.getElementById("blog-container");
    container.innerHTML = ""; // Clear previous content

    posts.forEach((post) => {
      const card = document.createElement("div");
      card.className = "blog-card animate-fade-in";

      const imageUrl = post.url || 'https://source.unsplash.com/random/800x600'; // Fallback image if none provided
      const trimmedSummary = limitWords(post.summary, 25);

      // Generate random tags for demo (remove this in later)
      const tags = ['Technology', 'Design', 'Writing', 'Productivity'];
      const randomTags = tags.sort(() => 0.5 - Math.random()).slice(0, 2);

      card.innerHTML = `
        <img src="${imageUrl}" alt="${post.title}" class="blog-card-image" />
        <div class="blog-card-content">
          <div class="blog-card-tags">
            ${randomTags.map(tag => `<span class="blog-tag">${tag}</span>`).join('')}
          </div>
          <h3>${post.title}</h3>
          <p>${trimmedSummary}</p>
          <div class="blog-meta">
            <div class="blog-meta-item">
              <svg width="16" height="16" fill="none" stroke="currentColor">
                <path d="M8 14s-3-2-3-5V4a3 3 0 1 1 6 0v5c0 3-3 5-3 5z"/>
                <path d="M17 8c0 6-9 8-9 8s-9-2-9-8a9 9 0 1 1 18 0z"/>
              </svg>
              <span>${post.created.split(" ")[0]}</span>
            </div>
            <div class="blog-meta-item">
              <button class="btn btn-outline save-btn" style="padding: 0.25rem 0.75rem">
                <svg width="16" height="16" fill="none" stroke="currentColor">
                  <path d="M19 21l-7-5-7 5V5a2 2 0 0 1 2-2h10a2 2 0 0 1 2 2z"/>
                </svg>
                Save
              </button>
              <button class="btn btn-primary read-btn" style="padding: 0.25rem 0.75rem">
                <svg width="16" height="16" fill="none" stroke="currentColor">
                  <path d="M5 12h14M12 5l7 7-7 7"/>
                </svg>
                Read
              </button>
            </div>
          </div>
        </div>
      `;

      container.appendChild(card);

      // Add event listeners for "Save" and "Read More" buttons
      const saveButton = card.querySelector(".save-btn");
      const readButton = card.querySelector(".read-btn");

      saveButton.addEventListener("click", () => {
        saveBtn(post.id);
        // Show the save toast
        const saveToast = document.getElementById('saveToast'); // saved notification pop-up
        saveToast.style.display = 'flex';
        setTimeout(() => {
          saveToast.style.display = 'none';
        }, 3000);
      });
      
      readButton.addEventListener("click", () => readBtn(post.id));
    });
  } 
  
const writeBtns =  document.querySelectorAll("#write");
  writeBtns.forEach( button => {
	button.addEventListener	("click", (e) => {
	      e.preventDefault();
	      write();
	  });
  })
  
  /*document.getElementById("write").addEventListener("click", (e) => {
      e.preventDefault();
      write();
  });*/

  async function write() {
      try {
          const res = await fetch("/manager/blog-write", {
              method: 'GET',
              credentials: 'include'
          });

          if (res.status === 403 || res.status == 401) {
              showModal();
          } else if (res.ok) {
              window.location.href = "/manager/blog-write";
          } else {
              alert("Something went wrong.");
          }
      } catch (err) {
          console.error("Error:", err);
          alert("Unexpected error occurred.");
      }
  }

  function showModal() {
      document.getElementById("accessDeniedModal").style.display = "block";
  }

  document.getElementById("closeBtn").addEventListener("click", () => closeBtn());

  function closeBtn(){
	document.getElementById("accessDeniedModal").style.display = "none";
  }


  // Helper to limit word count
  function limitWords(text, maxWords) {
    const words = text.split(" ");
    if (words.length <= maxWords) return text;
    return words.slice(0, maxWords).join(" ") + "...";
  }

  function readBtn(id) {
    window.location.href = `/read-blog2.html?postId=${id}`;
  }

  
  // for saving i need, user id, entity modifications, additionally i want to display role and username 
  async function saveBtn(id) {
    const res = await fetch(`/api/save-post?postId=${id}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json" 
      }
    });

    if (res.ok) {
      alert("Post: " + id + " saved successfully!");
    } else {
      alert("Failed to save Post: " + id);
    }
  }


  /*toggleBtn.addEventListener("click", () => {
    const html = document.documentElement;
    const currentTheme = html.getAttribute("data-bs-theme");
    const newTheme = currentTheme === "dark" ? "light" : "dark";
    html.setAttribute("data-bs-theme", newTheme);
    toggleBtn.textContent = newTheme === "dark" ? "☀️" : "🌙";
    updateButtonTheme();
  });*/

  loadMoreBtn.addEventListener("click", loadPosts);
  
  
  async function loadUser(){ // should load on home page
	
	// fetch request
	try{
		
		const res = await fetch("/api/user/me")
		
		if (!res.ok) {
		  if (res.status === 401) {
		    throw new Error("User not authenticated");
		  } else {
		    throw new Error(`Unexpected error: ${res.status}`);
		  }
		}

		const data = await res.json();
		console.log(data);
		
		// storing data in local storage - Role and username
		localStorage.setItem("userEmail", data.email);
		localStorage.setItem("userRole", data.role.slice(5)); // Only Show User
		
		// now use that data to fill up the coressponding fields - uersname and role
		const usernameRoleField = document.getElementById("username-role");
		loginBtn.style.display = "none";
		logoutBtn.style.display = "inline";
		
		// Update styling to match read-blog.js
		usernameRoleField.textContent = `${localStorage.getItem("userEmail")} (${localStorage.getItem("userRole")})`;
		usernameRoleField.style.display = "inline";
		usernameRoleField.style.marginRight = "1rem";
		usernameRoleField.style.color = "var(--text-secondary)";
		
		console.log(usernameRoleField.textContent);

	  } catch (err) {
	    console.error("Error:", err.message);
		
	    // clear stale data
	    localStorage.removeItem("userEmail");
	    localStorage.removeItem("userRole");
	  }
	}
	

  // Initial load
  loadPosts();
  loadUser();
  
  
  
});
