package olga.pietrzyk.androidteacher.login

class Articles(val id: String?, val title: String, val content: String, val email: String) {
    constructor() : this("", "", "", "") {
    }
}