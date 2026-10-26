document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("loginForm");
    const email = document.getElementById("email");
    const password = document.getElementById("password");
    const togglePassword = document.getElementById("togglePassword");
    const loginButton = document.getElementById("loginButton");
    const buttonText = document.getElementById("buttonText");


    // -------------------------------
    // Email validation
    // -------------------------------

    if (email) {

        email.addEventListener("input", function () {

            const emailPattern =
                /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/;

            if (emailPattern.test(this.value.trim())) {

                this.classList.remove("invalid");
                this.classList.add("valid");

            } else {

                this.classList.remove("valid");
                this.classList.add("invalid");

            }

        });

    }


    // -------------------------------
    // Password validation
    // -------------------------------

    if (password) {

        password.addEventListener("input", function () {

            if (this.value.length >= 6) {

                this.classList.remove("invalid");
                this.classList.add("valid");

            } else {

                this.classList.remove("valid");
                this.classList.add("invalid");

            }

        });

    }


    // -------------------------------
    // Show / Hide password
    // -------------------------------

    if (togglePassword && password) {

        togglePassword.addEventListener("click", function () {

            if (password.type === "password") {

                password.type = "text";
                this.textContent = "Hide";

            } else {

                password.type = "password";
                this.textContent = "Show";

            }

        });

    }


    // -------------------------------
    // Login form validation
    // -------------------------------

    if (form) {

        form.addEventListener("submit", function (event) {

            const emailPattern =
                /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/;

            const emailValid =
                email
                && emailPattern.test(email.value.trim());

            const passwordValid =
                password
                && password.value.length >= 6;


            // Check email

            if (!emailValid) {

                event.preventDefault();

                alert("Please enter a valid email address.");

                email.focus();

                return;
            }


            // Check password

            if (!passwordValid) {

                event.preventDefault();

                alert("Password must contain at least 6 characters.");

                password.focus();

                return;
            }


            // Disable button after valid submission

            if (loginButton) {

                loginButton.disabled = true;

            }

            if (buttonText) {

                buttonText.textContent = "Logging in...";

            }

        });

    }

});