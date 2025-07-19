
const userDetailTable = document.querySelector(".user-details");

async function loadAuthors(){
	try{
		// fetch the users with the manager roles
		const res = await fetch("/admin/api/authors")
		console.log(res)
		
		const data = await res.json();
		console.log(data);
		
		data.forEach((user, i) => {
					const tr = document.createElement("tr");
					tr.classList.add("user-row");     
					tr.id = `user-${i}`;  
					// Serial number
					const tdIndex = document.createElement("td");
					tdIndex.textContent = i + 1;

					// Name
					const tdName = document.createElement("td");
					tdName.textContent = user.name;

					// Email
					const tdEmail = document.createElement("td");
					tdEmail.textContent = user.email;
					
					// Add a backend logic so that admin dont get sent
					const tdBtn = document.createElement("button")
					tdBtn.classList.add("btn")
					tdBtn.addEventListener("click", () => revokeCall(user.email))
					//tdBtn.id = user.email; id cant have space or @ so not ideal use set attributes
					tdBtn.setAttribute("data-email", user.email)
					tdBtn.textContent = "Revoke"
					// Append all td to tr
					tr.appendChild(tdIndex);
					tr.appendChild(tdName);
					tr.appendChild(tdEmail);
					tr.appendChild(tdBtn)

					// Append tr to table
					userDetailTable.appendChild(tr);
				});
		
	}catch(err){
		console.error("Error fetching authors: " + err)
	}
}

loadAuthors();

async function revokeCall(userEmail){
	// implement your revoke logic here
	console.log("Revoke clicked for:", userEmail);
	try{
		
		const isConfirm = confirm("Please Confirm to revoke: " + userEmail + "'s Blog writing permission");
		
		if(!isConfirm){
			return
		}
		
		const res = await fetch(`/admin/api/authors/revoke?email=${encodeURIComponent(userEmail)}`, {
			method: "POST"
		})
		
		if(!res.ok){
			throw new Error("Revoke Failed")
		}
		
		alert(`Access revoked for ${userEmail}`);
		
	}catch(err){
		console.error("Error: " + err)
	}
}


