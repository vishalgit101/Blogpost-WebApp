
// logout script
const logoutBtn = document.getElementById("logoutBtn");

function logoutUser(){
	logoutBtn.addEventListener("click", (e) => {
		// prevent default
		e.preventDefault();
		
		// clear localstorge
		localStorage.removeItem("userEmail");
		localStorage.removeItem("userRole");
		window.location.href = "/logout"
	})
}

document.addEventListener("DOMContentLoaded", () =>{
	logoutUser();
})