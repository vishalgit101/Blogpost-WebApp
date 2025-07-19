
//const param = new URLSearchParams(window.location.search)
//const postId = param.get("postId");


// fetch comments for that specific posts 
async function fetchComments(postId){
	try{
		
		const res = await fetch(`/api/comments?postId=${postId}`);
		
		const data =  await res.json();
		console.log(data);
		if(data.length == 0){
			console.log("No Comments")
			
			// create a p element and add it to the dom and have the text content set as no comments
		}
		renderComments(data);
		
		// check for no commets and show no comments on the post
		
	}catch(err){
		console.error("Error: " + err);
	}
}

fetchComments(postId)
// render comments for that specific posts 
function renderComments(comments){
	
	comments.forEach( (comment) => {
		
		const parent = comment.parentId; 
		
		if(parent == null){ // meaning top level comment
			// Elements
			const commentListElement = document.createElement("div");
			commentListElement.classList.add("comment-list");
			const commentWrapper = document.createElement("div");
			commentWrapper.classList.add("comment-wrapper", "comment")
			const commentHeaderElement =  document.createElement("div");
			commentHeaderElement.classList.add("comment-header");
			const commentHeaderStrong = document.createElement("strong");
			const commentHeaderSpan = document.createElement("span");
			commentHeaderSpan.classList.add("time")
			const commentBodyElement = document.createElement("div");
			commentBodyElement.classList.add("comment-body")
			const commentBodyPara = document.createElement("p");
			commentBodyPara.classList.add("comment-body-text")
			const commentBodyReplyBtn = document.createElement("button");
			commentBodyReplyBtn.classList.add("reply-action")
			commentBodyReplyBtn.id = comment.id.toString() // parent id
			// selecting reply button and setting its id and adding event listener
			commentBodyElement.addEventListener("click", (e) => replyBtnFunction(e, comment.id))
			
			const name = comment.authorName; // Header - username, time created
			const time = comment.createdAt
			const commentBody = comment.comment;  // Body - text
			
			// Element - data integration
			commentHeaderSpan.textContent = `${time}`
			commentHeaderStrong.textContent = `${name}`
			commentBodyPara.textContent = `${commentBody}`
			commentBodyReplyBtn.textContent = "Reply"
			
			// appending
			commentHeaderElement.appendChild(commentHeaderStrong);
			commentHeaderElement.appendChild(commentHeaderSpan);
			commentBodyElement.appendChild(commentBodyPara)
			commentBodyElement.appendChild(commentBodyReplyBtn)
			
			// comment wrapper
			commentWrapper.appendChild(commentHeaderElement);
			commentWrapper.appendChild(commentBodyElement);
			
			//Comment List
			commentListElement.appendChild(commentWrapper)
			
			// check for replies for this comment (cuurent comment object replies), as it's parent however won't have any
			// replies as it's == null, so no infinite nested sttructure
			
			const replies = comment.replies;

			const replyListElement = document.createElement("div");
			replyListElement.classList.add("reply-list");
			if(replies.length > 0){ // meaning current comment object has replies
				
				replies.forEach( (reply) => { // reply also a comment
					
					const replyWrapper = document.createElement("div");
					replyWrapper.classList.add("reply-wrapper")
					
					const replyHeaderElement =  document.createElement("div");
					replyHeaderElement.classList.add("reply-header");
					const replyHeaderStrong = document.createElement("strong");
					const replyHeaderSpan = document.createElement("span");
					replyHeaderSpan.classList.add("time")
					
					const replyBodyElement = document.createElement("div");
					replyBodyElement.classList.add("reply-body")
					const replyBodyPara = document.createElement("p");
					replyBodyPara.classList.add("reply-body-text")
					
					const replyName = reply.authorName; // header - username, time
					const replyTime = reply.createdAt
					const replyBody = reply.comment // reply text
					
					// Element - data integration
					replyHeaderSpan.textContent = `${replyTime}`
					replyHeaderStrong.textContent = `${replyName}`
					replyBodyPara.textContent = `${replyBody}`
					
					// appending
					replyHeaderElement.appendChild(replyHeaderStrong);
					replyHeaderElement.appendChild(replyHeaderSpan);
					replyBodyElement.appendChild(replyBodyPara)
					
					// reply wrapper
					replyWrapper.appendChild(replyHeaderElement);
					replyWrapper.appendChild(replyBodyElement);
					
					//reply List
					replyListElement.appendChild(replyWrapper)
				})
				// reply list appending to comment wrapper 
				commentWrapper.appendChild(replyListElement)
			}else{
				console.log("No replies")
			}
			
			const commentsRoot = document.getElementById("comments-root");
			commentsRoot.appendChild(commentListElement)
		}	
		
	})
}


const submitBtn = document.getElementById("submit-btn")

submitBtn.addEventListener("click", async (e) =>{
	e.preventDefault();
	
	console.log("button clicked")
	
	const commmentBox = document.getElementById("text-field")
	const commentContent = commmentBox.value;

	const res = await fetch(`/api/comments?postId=${postId}`, {
		method:"POST",
		headers: {"Content-Type": "application/json"},
		body: JSON.stringify({commentContent})
	})
	
	if (res.ok) {
	        console.log("Comment submitted successfully");
			alert("Comment Posted")
			fetchComments(postId)
			commmentBox.textContent = "";
	    } else {
	        console.error("Failed to submit comment");
	    }
	
})

//const replyBtn = document.getElementById("reply-submit-btn")
function replyBtnFunction (e, id){

		e.preventDefault()
		console.log()
		console.log("Reply button clicked")
		console.log("Reply button under: " + "user")
		const commentBlock = e.target.closest(".comment");

		const replyField = document.querySelector(".reply-input-field");
		commentBlock.insertAdjacentElement("afterend", replyField);
		console.log(replyField);
		replyField.style.display="block"
		console.log(id + "reply btn parent id")
}
	
// This excrecuatinfg pain is kiillling me each day

