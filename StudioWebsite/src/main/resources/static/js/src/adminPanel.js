let currentUrl = "http://192.168.100.101:8080/";
let currentAlbum = '';
let imageObserver;
let currentAlbumData;

function createAccount() {
    document.location = currentUrl + "adminPanel/register";
}

function logoutCurrentUser() {
    document.location = currentUrl + "logout";
}

function changeDocForCurrentUser() {
    fetch(currentUrl + "adminPanel/connectedUser", {
        method: 'GET',
        headers: {
            'X-CSRF-TOKEN': csrfToken,
        }
    })
        .then(response => {
            if (!response.ok)
                throw new Error("Network response was not ok");
            else
                return response.json();
        })
        .then(data => {
            document.getElementById("username").innerHTML = data.name;
        });
}


document.addEventListener('DOMContentLoaded', function () {
    changeToolbarForNoSelected();
    changeDocForCurrentUser();
    loadAlbums();
});

document.addEventListener('DOMContentLoaded', function () {
    const lazyImages = document.querySelectorAll('.lazy-image');

    const options = {
        root: null,
        rootMargin: '0px',
        threshold: 0.5
    };

    function handleIntersection(entries, observer) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                loadImage(entry.target);
                observer.unobserve(entry.target)
            }
        })
    }

    function loadImage(image) {
        const src = image.getAttribute('data-src');
        if (src) {
            image.src = src;
        }
    }

    imageObserver = new IntersectionObserver(handleIntersection, options);
})


function openDownloadModal() {
    document.getElementById('downloadModal').style.display = 'block';
}

function closeDownloadModal() {
    document.getElementById('downloadModal').style.display = 'none';
}

function openSelectedDownloadModal() {
    document.getElementById('downloadSelectedModal').style.display = 'block';
}

function closeSelectedDownloadModal() {
    document.getElementById('downloadSelectedModal').style.display = 'none';
}

const createDownloadModal = () => {
    let downloadModal = document.createElement('div');
    downloadModal.classList.add("modal");
    downloadModal.setAttribute('id', 'downloadModal');
    let modalContent = document.createElement('div');
    modalContent.classList.add('modal-content');
    modalContent.innerHTML = '<span class="close" onClick="closeDownloadModal()">&times;</span>';
    let title = document.createElement('h2');
    title.setAttribute('id', 'title');
    // title.innerHTML="Download " + currentAlbum.album_title;

    let size = document.createElement('p');
    size.setAttribute('id', 'size');


    let downloadSectionContainer = document.createElement('div');
    downloadSectionContainer.classList.add('flex-container');


    let downloadButton = document.createElement('div');
    downloadButton.classList.add('modal-button');
    downloadButton.addEventListener('click', downloadAlbum);
    downloadButton.innerHTML = 'Download';
    downloadButton.setAttribute('id', 'modalDownloadButton');


    downloadSectionContainer.appendChild(downloadButton);


    modalContent.appendChild(title);
    modalContent.appendChild(size);
    modalContent.appendChild(downloadSectionContainer);
    downloadModal.appendChild(modalContent);
    return downloadModal;
}

const calculateSelectedContentSize = () => {
    let imageItems = Array.from(document.getElementsByClassName("selected"));
    let size = 0;

    imageItems.forEach(imageItem => {
        currentAlbumData.forEach(content => {
            //console.log(content.fileId);
            //console.log(imageItem.getAttribute("imageId"));
            if (content.fileId == imageItem.getAttribute("imageId")) {
                //console.log(content.fileSize);
                size = size + content.fileSize;
            }
        })
    })
    return size;
}


const changeDownloadModal = (modalId) => {

    let downloadModal = document.getElementById(modalId);
    let modalTitle = downloadModal.querySelector('#title');
    modalTitle.innerHTML = "Download " + currentAlbum.album_title;

    let size = document.querySelector('#size');

    size.innerHTML = "Size: " + processFileSize(currentAlbum.album_size);

}

const changeDownloadSelectedModal = (modalId) => {
    let downloadSelectedModal = document.getElementById(modalId);
    if (downloadSelectedModal) {
        let size = downloadSelectedModal.querySelector('#downloadSize');
        size.innerHTML = "Selection size: " + processFileSize(calculateSelectedContentSize())
        let selectedNum = downloadSelectedModal.querySelector('#selectedNumber');
        selectedNum.innerHTML = "Selected number: " + document.getElementsByClassName("selected").length.toString();
    }
}
const createDownloadSelectedModal = () => {
    let downloadModal = document.createElement('div');
    downloadModal.classList.add("modal");
    downloadModal.setAttribute('id', 'downloadSelectedModal');
    let modalContent = document.createElement('div');
    modalContent.classList.add('modal-content');
    modalContent.innerHTML = ' <span class="close" onClick="closeSelectedDownloadModal()">&times;</span>';
    let title = document.createElement('h2');
    title.innerHTML = "Download Selection";

    let size = document.createElement('p');
    size.setAttribute('id', 'downloadSize');

    let selectedNum = document.createElement('p');
    selectedNum.setAttribute('id', 'selectedNumber');


    let downloadSectionContainer = document.createElement('div');
    downloadSectionContainer.classList.add('flex-container');


    let downloadButton = document.createElement('div');
    downloadButton.classList.add('modal-button');
    downloadButton.addEventListener('click', downloadSelectedContent);
    downloadButton.innerHTML = 'Download';
    downloadButton.setAttribute('id', 'modalSelectedDownloadButton');


    downloadSectionContainer.appendChild(downloadButton);


    modalContent.appendChild(title);
    modalContent.appendChild(size);
    modalContent.appendChild(selectedNum);
    modalContent.appendChild(downloadSectionContainer);
    downloadModal.appendChild(modalContent);
    return downloadModal;
}

const checkIfExists = (elementID) => {
    let elem = document.getElementById(elementID);
    return elem != null;
}


function handleAlbumDownloadClick() {
    if (!checkIfExists('downloadModal')) {
        let downloadModal = createDownloadModal();
        document.body.appendChild(downloadModal);
    }
    changeDownloadModal('downloadModal');
    openDownloadModal();
}

function handleSelectedDownloadClick() {
    if (!checkIfExists('downloadSelectedModal')) {
        let downloadModal = createDownloadSelectedModal();
        document.body.appendChild(downloadModal);
    }
    changeDownloadSelectedModal('downloadSelectedModal');
    openSelectedDownloadModal();
}


function downloadAlbum() {

    let downloadButton = document.getElementById('modalDownloadButton');
    downloadButton.innerHTML = "Downloading...";  // Corrected property name
    downloadButton.style.pointerEvents = "none";

    const downloadLink = document.createElement('a');
    downloadLink.href = currentUrl + "adminPanel/downloadZipAlbum?albumTitle=" + currentAlbum.album_title;

    const filename = currentAlbum.album_title + ".zip";
    downloadLink.download = filename;
    downloadLink.style.display = 'none';


    document.body.appendChild(downloadLink);
    downloadLink.click();

    URL.revokeObjectURL(downloadLink.href);
    document.body.removeChild(downloadLink);
    downloadButton.innerHTML = 'Download';
    downloadButton.style.pointerEvents = "auto";

}

// async function downloadAlbum() {
//
//         let downloadButton = document.getElementById('modalDownloadButton');
//         downloadButton.innerHTML = "Downloading...";  // Corrected property name
//         downloadButton.style.pointerEvents = "none";
//         fetch(currentUrl+"adminPanel/downloadZipAlbum?albumTitle="+currentAlbum.album_title, {
//             method: 'GET',
//             headers: {
//                 // 'Accept-Encoding': 'gzip', // Request gzip encoding
//                 // 'X-CSRF-TOKEN': csrfToken,
//             },
//         })
//             .then(response => {
//                 // Check if the response is successful (status code 200)
//                 if (!response.ok) {
//                     throw new Error(`Failed to download file: ${response.statusText}`);
//                 }
//                 const link = document.createElement('a');
//
//                 // Create a Blob from the response
//                 return response.blob().then(blob => {
//                     const filename = currentAlbum.album_title + ".zip";
//
//
//
//                         const downloadLink = document.createElement('a');
//                         downloadLink.href = URL.createObjectURL(blob);
//                         downloadLink.download = filename;
//                         downloadLink.style.display = 'none';
//
//
//                         document.body.appendChild(downloadLink);
//                         downloadLink.click();
//
//                         URL.revokeObjectURL(downloadLink.href);
//                         document.body.removeChild(downloadLink);
//                         downloadButton.innerHTML='Download';
//                         downloadButton.style.pointerEvents = "auto";
//                 });
//             });
//
//     //     const filename = currentAlbum.album_title + ".zip";
//     //
//     //
//     //
//     //     const downloadLink = document.createElement('a');
//     //     downloadLink.href = URL.createObjectURL(fileBlob);
//     //     downloadLink.download = filename;
//     //     downloadLink.style.display = 'none';
//     //
//     //
//     //     document.body.appendChild(downloadLink);
//     //     downloadLink.click();
//     //
//     //     URL.revokeObjectURL(downloadLink.href);
//     //     document.body.removeChild(downloadLink);
//     //     downloadButton.innerHTML='Download';
//     //     downloadButton.style.pointerEvents = "auto";
//     //
//     // }
//     // catch (error) {
//     //     console.error('Error downloading album:', error);
//     //     // Handle the error, e.g., display an error message to the user
//     // }
//
//
// }


async function downloadSelectedContent() {
    let imageItems = Array.from(document.getElementsByClassName("selected"));
    let imageItemsIds = [];
    imageItems.forEach(imageItem => {
        let imageId = imageItem.getAttribute("imageId");
        imageItemsIds.push(imageId);
    })


    //change the downloadButton to not allow several downloads at the same time
    let downloadButton = document.getElementById('modalSelectedDownloadButton');
    downloadButton.innerHTML = "Downloading...";  // Corrected property name
    downloadButton.style.pointerEvents = "none";


    $.ajax({
        xhr: function () {// Seems like the only way to get access to the xhr object
            var xhr = new XMLHttpRequest();
            xhr.responseType = 'blob'
            return xhr;
        },
        type: "POST",
        headers: {
            'X-CSRF-TOKEN': csrfToken,
        },
        url: currentUrl + "adminPanel/downloadZipContent",
        data: {
            contentIds: imageItemsIds
        },
        success: function (response) {
            console.log(response);
            const filename = "contentDownload.zip";
            const fileBlob = response;


            const downloadLink = document.createElement('a');
            downloadLink.href = URL.createObjectURL(fileBlob);
            downloadLink.download = filename;
            downloadLink.style.display = 'none';


            document.body.appendChild(downloadLink);
            downloadLink.click();

            URL.revokeObjectURL(downloadLink.href);
            document.body.removeChild(downloadLink);


            //change the button again
            downloadButton.innerHTML = 'Download';
            downloadButton.style.pointerEvents = "auto";

        },
        error: function (e) {
            throw new Error("Request failed: " + e);
        }
    });
}

function deleteSelected() {
    let imageItems = Array.from(document.getElementsByClassName("selected"));
    let promises = [];

    imageItems.forEach(imageItem => {
        let imageId = imageItem.getAttribute("imageId");
        let url = currentUrl + "adminPanel?removeContent&id=" + imageId + "&albumId=" + currentAlbum.album_title;

        let promise = fetch(url, {
            method: 'POST',
            headers: {
                'X-CSRF-TOKEN': csrfToken,
            }
        }).then(response => {
            if (!response.ok) {
                throw new Error("Couldn't delete file");
            }
        });

        promises.push(promise);
    });

    Promise.all(promises)
        .then(() => {
            displayAlbumData(currentAlbum);
            changeToolbarForNoSelected();

        })
        .catch(error => {
            alert(error.message); // Display error message if any image deletion fails
        });
}


function handleAlbumClick(album) {
    currentAlbum = album;
    let currentAlbumInfoButton = document.getElementById("current-album-title");
    currentAlbumInfoButton.innerHTML = currentAlbum.album_title;
    currentAlbumInfoButton.classList.replace("toolbar-info-button-hidden", "toolbar-info-button");
}

function loadAlbums() {
    var albumContainer = document.getElementById('albumSideBar');
    fetch('/adminPanel?getAlbums')
        .then(response => response.json())
        .then(albums => {
            albumContainer.innerHTML = '';
            albums.forEach(album => {
                let albumDiv = document.createElement('div');
                albumDiv.classList.add('album');
                albumDiv.addEventListener('click', function () {

                    handleAlbumClick(album);
                    // Handle the click event (e.g., navigate to a detailed view)


                    displayAlbumData(currentAlbum);
                });

                let titleElement = document.createElement('strong');
                titleElement.textContent = album.album_title + " ";
                albumDiv.appendChild(titleElement)

                let usernameElement = document.createElement('strong');
                usernameElement.textContent = album.username + " ";
                albumDiv.appendChild(usernameElement)

                let creationDateElement = document.createElement('strong');
                creationDateElement.textContent = processTime(album.creation_date);
                albumDiv.appendChild(creationDateElement)
                albumContainer.appendChild(albumDiv);
            });
        })
}

let csrfToken = document.querySelector('meta[name="_csrf"]').content;



//this can be improved(currently inefficient due to number of requests made)
function uploadFile() {
    const fileInput = document.getElementById('fileInput');
    const uploadPromises = [];

    for (let i = 0; i < fileInput.files.length; i++) {
        const file = fileInput.files[i];
        if (file) {
            const formData = new FormData();
            formData.append('file', file);
            formData.append('albumName', currentAlbum.album_title);

            const uploadPromise = fetch(currentUrl + 'adminPanel?upload', {
                method: 'POST',
                body: formData,
                headers: {
                    'X-CSRF-TOKEN': csrfToken,
                }
            }).then(response => {
                if (!response.ok) {
                    throw new Error("Couldn't upload file");
                }
            });

            uploadPromises.push(uploadPromise);
        }
    }
    fileInput.value = null;


    Promise.all(uploadPromises)
        .then(() => {

            displayAlbumData(currentAlbum);
        })
        .catch(error => {
            alert(error.message); // Display error message if any file upload fails
        });
}

function createAlbum() {
    let albumTitle = document.getElementById('albumTitle').value;
    if (albumTitle) {
        fetch(currentUrl + 'adminPanel?createAlbum', {
            method: 'POST',
            body: JSON.stringify({
                album_title: albumTitle,
                username: 'root'
            }),
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'X-CSRF-TOKEN': csrfToken,
            }
        })
            .then(function (response) {
                window.location.href = response.url;
            })
    }
}


function displayAlbumData(album) {
    let url = currentUrl + 'adminPanel/album?id=' + album.album_title;
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log("This workes");
            displayThumbnails(data);
            currentAlbumData = data;

        })
        .catch(error => alert("Error fetching album data"));
}


const processTime = (time) => {
    let stringTime = time.toString();
    let date = stringTime.substring(0, stringTime.indexOf("T"));
    let hourMinuteSeconds = stringTime.substring(stringTime.indexOf("T") + 1);
    let hourMinute = hourMinuteSeconds.substring(0, hourMinuteSeconds.indexOf(".") - 3);
    return hourMinute + " " + date;
}

function processFileSize(fileSize) {
    if (fileSize > 1000) {
        fileSize = fileSize / 1000;
        return fileSize.toFixed(2).toString() + " GB";
    }

    return fileSize.toFixed(2).toString() + " MB";
}

const createToolbarInfoButton = () => {
    let currentSelectedNumInfoButton = document.createElement("div");
    currentSelectedNumInfoButton.classList.add("toolbar-info-button");
    return currentSelectedNumInfoButton;
}

const changeToolbarInfoSectionForSelected = (selectedPhotos) => {
    let toolbarInfoSection = document.getElementById("toolbar-info-section");
    toolbarInfoSection.innerHTML = '';
    let currentSelectedNumInfoButton = createToolbarInfoButton();
    currentSelectedNumInfoButton.innerHTML = "Selected: " + selectedPhotos.length.toString();
    toolbarInfoSection.appendChild(currentSelectedNumInfoButton);
}

const createToolbarButton = () => {
    let button = document.createElement("div");
    button.classList.add("toolbar-button");
    return button;
}

const createToolbarSelectionButton = () => {
    let button = document.createElement("div");
    button.classList.add("selection-toolbar-button");
    return button;
}
const changeToolbarButtonSectionForSelected = () => {
    if (document.getElementById("toolbar-delete-selected-button") == null) {
        let toolbarButtonSection = document.getElementById("toolbar-button-section");
        toolbarButtonSection.innerHTML = '';

        let deleteSelectedButton = createToolbarSelectionButton();
        deleteSelectedButton.innerHTML = "Delete";
        deleteSelectedButton.setAttribute('id', 'toolbar-delete-selected-button');
        deleteSelectedButton.addEventListener('click', deleteSelected);


        let downloadSelectedButton = createToolbarSelectionButton();

        downloadSelectedButton.innerHTML = "Download";
        downloadSelectedButton.setAttribute('id', 'toolbar-download-selected-button');
        downloadSelectedButton.addEventListener('click', handleSelectedDownloadClick);

        //to implement donwload selected button

        toolbarButtonSection.appendChild(deleteSelectedButton);
        toolbarButtonSection.appendChild(downloadSelectedButton);
    }
}

const changeToolbarForSelected = (selectedPhotos) => {
    let toolbar = document.getElementById("toolbar");
    toolbar.classList.replace("toolbar", "selection-toolbar");
    changeToolbarInfoSectionForSelected(selectedPhotos);
    changeToolbarButtonSectionForSelected();
}

const changeToolbarButtonSectionForNoSelected = () => {
    if (document.getElementById("toolbar-upload-button") == null) {
        let toolbarButtonSection = document.getElementById("toolbar-button-section");


        toolbarButtonSection.innerHTML = '';

        var fileUploadForm = document.createElement('form');
        fileUploadForm.id = 'fileUploadForm';
        fileUploadForm.style.height = '1px';

// Creating the input element
        var fileInput = document.createElement('input');
        fileInput.type = 'file';
        fileInput.name = 'files[]';
        fileInput.id = 'fileInput';
        fileInput.multiple = true;
        fileInput.style.display = 'none';
        fileInput.addEventListener('change', uploadFile); // assuming uploadFile is a defined function


        let uploadButton = createToolbarButton();
        uploadButton.innerHTML = 'Upload'
        uploadButton.setAttribute('id', 'toolbar-upload-button');
        uploadButton.addEventListener('click', function () {
            document.getElementById('fileInput').click();
        });

        fileUploadForm.appendChild(fileInput);
        fileUploadForm.appendChild(uploadButton);


        let downloadButton = createToolbarButton();
        downloadButton.setAttribute('id', "toolbar-download-button");
        downloadButton.addEventListener('click', handleAlbumDownloadClick);

        downloadButton.innerHTML = "Download";

        let sortButton = createToolbarButton();
        sortButton.setAttribute('id', 'toolbar-sort-button');
        //to implement sort function
        sortButton.innerHTML = "Sort";

        // toolbarButtonSection.appendChild(uploadButton);
        toolbarButtonSection.appendChild(downloadButton);
        toolbarButtonSection.appendChild(sortButton);
        toolbarButtonSection.appendChild(fileUploadForm);
    }
}

const changeToolbarInfoSectionForNoSelected = () => {
    let toolbarInfoSection = document.getElementById("toolbar-info-section");
    toolbarInfoSection.innerHTML = '';
    let currentSelectedNumInfoButton = createToolbarInfoButton();
    currentSelectedNumInfoButton.innerHTML = currentAlbum.album_title;
    currentSelectedNumInfoButton.setAttribute('id', 'current-album-title');
    toolbarInfoSection.appendChild(currentSelectedNumInfoButton);
}

const changeToolbarForNoSelected = () => {
    let toolbar = document.getElementById("toolbar");
    toolbar.classList.replace("selection-toolbar", "toolbar");
    changeToolbarInfoSectionForNoSelected();
    changeToolbarButtonSectionForNoSelected()
}



function displayThumbnails(contents) {
    const createImageItem = (content) => {
        const imageItem = document.createElement('div');
        imageItem.setAttribute('imageId', content.fileId);
        imageItem.classList.add('image-item');
        // imageItem.classList.toggle('unselected');
        let imgElement = createImageElement(content);
        let infoBar = createInfoBar(content);
        let selectionIndicator = document.createElement("div");
        selectionIndicator.classList.add("selection-indicator");
        selectionIndicator.innerHTML = '<span class="tick-mark">✔</span>';
        selectionIndicator.addEventListener('click', function () {
            imageItem.classList.toggle("selected");
            event.stopPropagation();
            const selectedPhotos = document.querySelectorAll(".selected");
            if (selectedPhotos.length > 0)
                changeToolbarForSelected(selectedPhotos);
            else {
                changeToolbarForNoSelected();
            }
        })

        imageItem.appendChild(imgElement);
        imageItem.appendChild(selectionIndicator);
        imageItem.appendChild(infoBar);
        return imageItem;
    };


    const createInfoBar = (content) => {
        const infoBar = document.createElement('div');
        infoBar.classList.add('content-info-bar')
        infoBar.innerText = content.fileName;
        return infoBar;
    }


    const createImageElement = (content) => {
        const imgElement = document.createElement('img');
        imgElement.setAttribute('data-src', currentUrl + 'adminPanel/thumbnails?id=' + content.fileId + "&thumbnailWidth=200&thumbnailHeight=200");
        imageObserver.observe(imgElement);
        return imgElement;
    }

    const createFullResImgItem = () => {
        let fullResImgItem = document.createElement("div");
        fullResImgItem.classList.add("full-res-image-item");
        return fullResImgItem;
    }

    const createFullResImg = (content) => {
        let fullResImg = document.createElement("img");
        let url = currentUrl + 'adminPanel/content?id=' + content.fileId;
        fullResImg.classList.add("full-res-image-item-img");
        fullResImg.src = url;
        return fullResImg;
    }

    const createImageDetails = (content) => {
        const imageDetails = document.createElement("div");
        imageDetails.classList.add("content-details");

        let imageName = document.createElement("h2");
        imageName.classList.add("image-name");
        imageName.textContent = content.fileName;

        let imageUploader = document.createElement("p");
        imageUploader.classList.add("image-uploader");
        imageUploader.textContent = "Uploader: " + content.fileUploader;

        let imageUploadTime = document.createElement("p");
        imageUploadTime.classList.add("image-upload-time");
        imageUploadTime.textContent = "Upload date: " + processTime(content.uploadDate);

        let imageSize = document.createElement("p");
        imageSize.classList.add("image-size");
        let fileSizeString = processFileSize(content.fileSize);
        imageSize.textContent = "Size: " + fileSizeString;

        imageDetails.appendChild(imageName);
        imageDetails.appendChild(imageUploader);
        imageDetails.appendChild(imageUploadTime);
        imageDetails.appendChild(imageSize);

        return imageDetails;
    };


    const createContentInfoContainer = () => {
        const contentInfoContainer = document.createElement("div");
        contentInfoContainer.classList.add("content-info-container");
        contentInfoContainer.id = "content-info";

        return contentInfoContainer;
    }

    const createLoadingCircle = () => {
        const loadingCircle = document.createElement("div");
        loadingCircle.id = "loading-circle";
        loadingCircle.classList.add("loading-circle");
        loadingCircle.style.display = "block"; // Show the loading circle
        return loadingCircle;
    }


    const handleImageDbClick = (content) => {
        let imageInfoContainer = document.getElementById("content-info");

        if (!imageInfoContainer) {
            imageInfoContainer = createContentInfoContainer();
            let imageContentInfoContainer = document.getElementById("image-content-info-container");
            imageContentInfoContainer.appendChild(imageInfoContainer);
        }
        imageInfoContainer.style.display = "block";
        let imageContainer = document.getElementById("imageContainer");
        imageContainer.classList.replace("image-container-extended", "image-container-non-extended");

        const closeButton = document.createElement("button");
        closeButton.innerHTML = "&times;"; // Use the HTML entity for "times" (X)
        closeButton.classList.add("close-button");
        closeButton.addEventListener("click", () => {
            imageInfoContainer.style.display = "none"; // Hide the container
            imageContainer.classList.replace("image-container-non-extended", "image-container-extended");
        });

        imageInfoContainer.innerHTML = '';
        imageInfoContainer.appendChild(closeButton);
        let loadingCircle = createLoadingCircle();
        imageInfoContainer.appendChild(loadingCircle);


        let fullResImg = createFullResImg(content);
        let fullResImgItem = createFullResImgItem();
        fullResImgItem.appendChild(fullResImg);

        let imageDetails = createImageDetails(content);

        fullResImg.onload = function () {

            let loadingCircle = document.getElementById("loading-circle");
            if (loadingCircle) {
                loadingCircle.style.display = "none";
            }
            imageInfoContainer.appendChild(fullResImgItem);
            imageInfoContainer.appendChild(imageDetails);
        };
    }


    const container = document.getElementById('imageContainer');
    container.innerHTML = '';
    contents.forEach(content => {
        if (content.fileType === 'image') {
            const imageItem = createImageItem(content);
            imageItem.addEventListener('click', function () {
                handleImageDbClick(content);
            });
            container.appendChild(imageItem);


        } else if (content.fileType === 'video') {


            ///will implement later

            // const imageItem = document.createElement('div');
            // imageItem.setAttribute('imageId', content.fileId);
            // imageItem.classList.add('image-item');


            const videoItem = document.createElement('div');
            videoItem.setAttribute('videoId', content.fileId);
            videoItem.classList.add('image-item');
            videoItem.addEventListener('click', function () {
                videoItem.classList.toggle("selected");
            });

            videoItem.addEventListener('dblclick', function () {
                let contentInfoContainer = document.getElementById("content-info");
                contentInfoContainer.innerHTML = ' <div id="loading-circle" class="loading-circle"></div>'; // Clear the existing content
                let loadingCircle = document.getElementById("loading-circle");
                loadingCircle.style.display = "block"; // Show the loading circle

                let url = currentUrl + 'adminPanel/content?id=' + content.fileId;

                let fullVideoItem = document.createElement("div");
                fullVideoItem.classList.add("full-video-item");

                let fullVideo = document.createElement("video");
                fullVideo.classList.add("full-video-item-video");
                fullVideo.src = url;

                fullVideo.addEventListener('loadedmetadata', function () {
                    loadingCircle.style.display = "none";
                    contentInfoContainer.appendChild(fullVideoItem);
                    contentInfoContainer.appendChild(videoDetails);
                });
                fullVideoItem.appendChild(fullVideo);

                let videoDetails = document.createElement("div");
                videoDetails.classList.add("content-details");

                let videoName = document.createElement("h2");
                videoName.classList.add("video-name");
                videoName.textContent = content.fileName;

                let videoUploader = document.createElement("p");
                videoUploader.classList.add("video-uploader");
                videoUploader.textContent = "Uploader: " + content.fileUploader;

                let videoUploadTime = document.createElement("p");
                videoUploadTime.classList.add("video-upload-time");
                videoUploadTime.textContent = "Upload date: " + content.uploadDate;

                let videoSize = document.createElement("p");
                videoSize.classList.add("image-size");
                let fileSizeString = processFileSize(content.fileSize);
                videoSize.textContent = "Size: " + fileSizeString;

                videoDetails.appendChild(videoName);
                videoDetails.appendChild(videoUploader);
                videoDetails.appendChild(videoUploadTime);
                videoDetails.appendChild(videoSize);

            });
            const imgElement = document.createElement('img');
            imgElement.setAttribute('data-src', currentUrl + 'adminPanel/thumbnails?id=' + content.fileId + "&thumbnailWidth=200&thumbnailHeight=200");
            imageObserver.observe(imgElement);

            const videoElement = document.createElement('img');
            videoElement.src = currentUrl + 'adminPanel/thumbnails?id=' + content.fileId + "&thumbnailWidth=200&thumbnailHeight=200";
            imageObserver.observe(videoElement);

            const infoBar = document.createElement('div');
            infoBar.classList.add('content-info-bar')
            infoBar.innerText = content.fileName;
            // Append the video element to the video item
            videoItem.appendChild(videoElement);
            videoItem.appendChild(infoBar);
            // Append the video item to the container
            container.appendChild(videoItem);
        }
    });
}
