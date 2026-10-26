document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("registerForm");

    const firstName = document.getElementById("firstName");
    const lastName = document.getElementById("lastName");
    const email = document.getElementById("email");
    const mobile = document.getElementById("mobile");
    const password = document.getElementById("password");

    const terms = document.getElementById("terms");
    const togglePassword = document.getElementById("togglePassword");
    const mobileMessage = document.getElementById("mobileMessage");

    const registerButton = document.getElementById("registerButton");
    const buttonText = document.getElementById("buttonText");


    /* ================================
       MOBILE NUMBER
       ONLY 10 DIGITS
    ================================= */

    if (mobile) {

        mobile.addEventListener("input", function () {

            // Remove anything that is not a number
            this.value = this.value.replace(/[^0-9]/g, "");

            // Maximum 10 digits
            if (this.value.length > 10) {
                this.value = this.value.substring(0, 10);
            }

            // Validation message
            if (mobileMessage) {

                if (this.value.length === 10) {

                    this.classList.remove("invalid");
                    this.classList.add("valid");

                    mobileMessage.textContent =
                        "Mobile number looks good.";

                } else {

                    this.classList.remove("valid");
                    this.classList.add("invalid");

                    mobileMessage.textContent =
                        "Enter exactly 10 digits.";
                }
            }
        });
    }


    /* ================================
       NAME VALIDATION
    ================================= */

    function validateName(input) {

        if (!input) {
            return false;
        }

        const value = input.value.trim();

        if (value.length >= 2) {

            input.classList.remove("invalid");
            input.classList.add("valid");

            return true;

        } else {

            input.classList.remove("valid");
            input.classList.add("invalid");

            return false;
        }
    }


    if (firstName) {

        firstName.addEventListener("input", function () {
            validateName(this);
        });

    }


    if (lastName) {

        lastName.addEventListener("input", function () {
            validateName(this);
        });

    }


    /* ================================
       EMAIL VALIDATION
    ================================= */

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


    /* ================================
       PASSWORD VALIDATION
    ================================= */

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


    /* ================================
       SHOW / HIDE PASSWORD
    ================================= */

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


    /* ================================
       FORM SUBMISSION
    ================================= */

    if (form) {

        form.addEventListener("submit", function (event) {

            const firstNameValid =
                validateName(firstName);

            const lastNameValid =
                validateName(lastName);

            const emailPattern =
                /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/;

            const emailValid =
                email
                ? emailPattern.test(email.value.trim())
                : false;

            const mobileValid =
                mobile
                ? /^\d{10}$/.test(mobile.value.trim())
                : false;

            const passwordValid =
                password
                ? password.value.length >= 6
                : false;

            const termsValid =
                terms
                ? terms.checked
                : true;


            /* FIRST NAME */

            if (!firstNameValid) {

                event.preventDefault();

                alert("Please enter a valid first name.");

                if (firstName) {
                    firstName.focus();
                }

                return;
            }


            /* LAST NAME */

            if (!lastNameValid) {

                event.preventDefault();

                alert("Please enter a valid last name.");

                if (lastName) {
                    lastName.focus();
                }

                return;
            }


            /* EMAIL */

            if (!emailValid) {

                event.preventDefault();

                alert("Please enter a valid email address.");

                if (email) {
                    email.focus();
                }

                return;
            }


            /* MOBILE */

            if (!mobileValid) {

                event.preventDefault();

                alert(
                    "Mobile number must contain exactly 10 digits."
                );

                if (mobile) {
                    mobile.focus();
                }

                return;
            }


            /* PASSWORD */

            if (!passwordValid) {

                event.preventDefault();

                alert(
                    "Password must contain at least 6 characters."
                );

                if (password) {
                    password.focus();
                }

                return;
            }


            /* TERMS */

            if (!termsValid) {

                event.preventDefault();

                alert(
                    "Please accept the terms and conditions."
                );

                if (terms) {
                    terms.focus();
                }

                return;
            }


            /* ================================
               DISABLE BUTTON WHILE SUBMITTING
            ================================= */

            if (registerButton) {

                registerButton.disabled = true;
            }

            if (buttonText) {

                buttonText.textContent =
                    "Creating Account...";
            }

        });
    }

});