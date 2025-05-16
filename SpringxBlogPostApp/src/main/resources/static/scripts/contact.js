
async function contactAdmin(e){
	
	console.log("Check 1")
	
	e.preventDefault();
	
	const firstName = document.querySelector(".first-name").value;
	const lastName = document.querySelector(".last-name").value;
	const email = document.querySelector(".email").value;
	const phoneNumber = document.querySelector(".phone").value;
	const des = document.querySelector(".description").value;
	
	try{
		console.log("Check 2")
		const res = await fetch("/api/write-permission", {
			method: "POST", 
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({firstName, lastName,email, phoneNumber, des})
			
			
		})
		console.log("Check 3")
		
		if(res.ok){
			alert("Request Sent Successfully!")
		}
		
	}catch(err){
		console.error("Error: " + err);
	}
}

document.addEventListener("DOMContentLoaded", function () {
  document.querySelector("#contactForm").addEventListener("submit", contactAdmin); 
});