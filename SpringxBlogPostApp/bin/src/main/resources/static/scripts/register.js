// Func - 1
async function registerUser(event) {
  // Prevent Default - 2
  event.preventDefault();

  // doc fields - 3
  const username = document.querySelector(".username").value;
  const email = document.querySelector(".email").value;
  const password = document.querySelector(".password").value;

  // await fetch - 4, 5
  const response = await fetch("/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" }, // Fixed 'header' to 'headers'
    body: JSON.stringify({ username, email, password }), // Fixed 'Stringify' to 'stringify'
  });

  // Conditional - 6
  if (response.status === 201) {
    // Fixed response check
    alert("Signup successful! Redirecting to login...");
    window.location.href = "/login.html"; // Fixed 'windows' to 'window'
  } else {
    alert("Try again... Something went wrong!");
  }
}

// Function calling - 7
document.addEventListener("DOMContentLoaded", function () {
  document.querySelector(".register").addEventListener("submit", registerUser); // Fixed function name
});
