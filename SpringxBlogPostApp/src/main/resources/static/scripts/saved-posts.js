// Initialize the page
document.addEventListener('DOMContentLoaded', () => {
    loadSavedPosts();
    setupEventListeners();
});

// Api call
async function loadSavedPosts() {
    //const savedPosts = JSON.parse(localStorage.getItem('savedPosts')) || [];
    const postsList = document.querySelector('.saved-posts-list');
	
    try{
		const res = await fetch("/api/save-post")
		const data  = await res.json()
		console.log(data + " Data Lenght: " +data.length)
		savedPosts = data;
		console.log(savedPosts)
		if (savedPosts.length === 0) {
		    postsList.innerHTML = `
		        <div class="no-posts">
		            <p>You haven't saved any posts yet.</p>
		        </div>
		    `;
		    return;
		}
		
							/*<div class="post-tags">
			                   ${post.tags.map(tag => `<span class="tag">${tag}</span>`).join('')}
			                </div>*/
		
		savedPosts.forEach(post => {
		    const postDiv = document.createElement('div');
		    postDiv.className = 'saved-post';
		    postDiv.dataset.id = post.id;
	
		    postDiv.innerHTML = `
		        <div class="post-image">
		            <img src="${post.url}" alt="${post.title}">
		        </div>
		        <div class="post-content">
		            <div class="post-meta">
		                <span class="author">${post.authorUsername}</span>
		                <span class="date">${post.created}</span>
		            </div>
		            <h3 class="post-title">${post.title}</h3>
		            <p class="post-excerpt">${post.summary}</p>
		            <div class="post-actions">
		                <button class="remove-saved" title="Remove from saved">
		                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
		                        <path d="M3 6h18"></path>
		                        <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6"></path>
		                        <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2"></path>
		                    </svg>
		                </button>
		            </div>
		        </div>
		    `;
	
		    postsList.appendChild(postDiv);
		});

		
	}catch(err){
		console.error("Unable to load saved posts" + err)
	}
	
	


   
}

// Setup event listeners
function setupEventListeners() {
    // Handle remove from saved
    document.querySelector('.saved-posts-list').addEventListener('click', (e) => {
        if (e.target.closest('.remove-saved')) {
            const post = e.target.closest('.saved-post');
            const postId = post.dataset.id;
            removeFromSaved(postId);
            post.remove();
            showToast('Post removed from your reading list');
        }
    });

    // Handle post click (redirect to read page)
    document.querySelector('.saved-posts-list').addEventListener('click', (e) => {
        if (e.target.closest('.saved-post') && !e.target.closest('.remove-saved')) {
            const post = e.target.closest('.saved-post');
            const postId = post.dataset.id;
            window.location.href = `read-blog.html?id=${postId}`;
        }
    });
}

// Remove post from saved
function removeFromSaved(postId) {
    const savedPosts = JSON.parse(localStorage.getItem('savedPosts')) || [];
    const updatedPosts = savedPosts.filter(post => post.id !== postId);
    localStorage.setItem('savedPosts', JSON.stringify(updatedPosts));
}

// Show toast notification
function showToast(message) {
    const toastContainer = document.querySelector('.toast-container');
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerHTML = `
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
            <polyline points="22 4 12 14.01 9 11.01"></polyline>
        </svg>
        <span>${message}</span>
    `;
    
    toastContainer.appendChild(toast);
    
    setTimeout(() => {
        toast.remove();
    }, 3000);
} 