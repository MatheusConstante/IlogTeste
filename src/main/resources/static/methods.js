function createUser() {
    $.ajax({
        url: "localhost:8080/api/courses/2",
        //method: "PATCH",
        //url: "/learn/api/public/v1/courses/" + courseId + "/contents/" + contentId + "/users/_36808_1/reviewStatus",

        success: function (response) {
            console.log(response);
        },
        error: function (response) {
            console.log(response);
        }
    });
}