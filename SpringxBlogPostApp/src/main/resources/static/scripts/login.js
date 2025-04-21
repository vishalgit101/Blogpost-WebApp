console.log("Random 1");

/* async function loginUser(event) {
    event.preventDefault(); // Prevent default form submission
    console.log("Random 2");
    alert("JS is running");
    console.log("Random 3");
    const username = document.querySelector(".username").value; // could have kept email everywhere, and just changed here
    const password = document.querySelector(".password").value;
    
    console.log(username, password);

    const response = await fetch("/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    });

    if (response.ok) {
        window.location.href = "/home.html"; // ✅ Redirect to dashboard
    } else {
        document.querySelector(".message").textContent = "Invalid email or password!";
    }
}*/

async function login(event) {
  event.preventDefault();

  const username = document.querySelector(".username").value;
  const password = document.querySelector(".password").value;

  const formData = new URLSearchParams();
  formData.append("username", username);
  formData.append("password", password);

  try {
    const response = await fetch("/login", {
      method: "Post",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: formData,
      credentials: "include", // Important to include cookies (like JSESSIONID)
    });

    // ✅ Even if Spring redirects, fetch will not update the URL
    if (response.redirected) {
      // Manually update browser location
      window.location.href = response.url;
    } else if (response.ok) {
      // Sometimes it might return 200 instead (custom setups)
      window.location.href = "/home";
    } else {
      document.querySelector(".message").textContent =
        "Invalid username or password!";
    }
  } catch (err) {}
}

//document.querySelector(".login").addEventListener("submit", loginUser);
/* document.addEventListener("DOMContentLoaded", function () {
    document.querySelector(".login").addEventListener("submit", loginUser);
});*/
document.addEventListener("DOMContentLoaded", function () {
  document.querySelector(".login").addEventListener("submit", login);
});
//document.querySelector(".login").addEventListener("submit", login);
