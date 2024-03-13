# Studio Website

This website allows users to upload albums to a local server and a local SQL Server Database. The admin has additional privileges to delete and download pictures from specific albums.

## Features

1. **Main Page**
    - In progress: The main page will showcase and advertise the services of the business.

2. **Admin Panel (/adminPanel)**
    - Secure Access: The admin can access the `/adminPanel` endpoint, which is secured by Spring Security. Unauthorized attempts will prompt users to log in.

    - **Functionality:**
        - **Upload Files:** Admins can upload files to the server.
        - **Delete Files:** Admins can delete files from the server.
        - **Download Files:** Admins can download files from the server.

    - **Upcoming Feature:**
        - **Download Links:** Admins can create download links for users to download specific pictures.

## Screenshots

![Admin Panel 1](https://github.com/912-Contiu-Mario/studioWebsite/assets/115422643/6b7103a6-6b27-45a7-84fa-aaa4852dc7e3)

![Admin Panel 2](https://github.com/912-Contiu-Mario/studioWebsite/assets/115422643/08f0b345-0c0c-43f8-8749-5e0de2c55680)

![Admin Panel 3](https://github.com/912-Contiu-Mario/studioWebsite/assets/115422643/398b143d-df4b-4105-8209-d02612256f4c)

## Security

The `/adminPanel` endpoint is protected by Spring Security. Attempting to access it without proper authentication will redirect users to the login page.

![Login Prompt](https://github.com/912-Contiu-Mario/studioWebsite/assets/115422643/4afb20a3-2fcd-48ed-bf87-62f4682a4aea)
