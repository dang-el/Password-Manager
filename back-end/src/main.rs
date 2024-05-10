use std::collections::HashMap;
use actix_web::{web, App, HttpResponse, HttpServer};
use std::sync::{Arc, Mutex};
use rusqlite::{Result, params, Connection , Error as RusqliteError};

#[derive(Clone)]
struct AppState {
    db_conn: Arc<Mutex<Connection>>,
}

#[derive(Debug)]
struct Account {
    id: i64,
    username: String,
    password: String,
    email: String,
    phone_number: String
}

#[allow(dead_code)]
#[derive(Debug)]
struct Login{
    id: i64,
    service_name: String,
    account_nickname: String,
    username: String,
    password: String,
    site_url: String,
}

#[actix_web::main]
async fn main() -> Result<(), std::io::Error> {
    // Get the current directory
    let current_dir = std::env::current_dir().expect("Failed to get current directory");
    // Construct the absolute path to the database file
    let db_path = current_dir.join("src/db/PasswordManager.db");
    // Open the database connection
    let conn = Arc::new(Mutex::new(rusqlite::Connection::open(&db_path).unwrap()));
    // Create an instance of AppState
    let app_state = AppState { db_conn: conn };
    // Establish server and route requests
    HttpServer::new(move || {
        App::new()
            .app_data(web::Data::new(app_state.clone())) // Pass AppState (database connection) to all handlers
            .route("/signup", web::post().to(signup_post_handler))
            .route("/login", web::post().to(login_post_handler))
            .route("/", web::post().to(main_post_handler))
            .route("/forgot-password", web::post().to(forgot_password_post_handler))
            .route("/forgot-password/change-password", web::put().to(change_password_put_handler))
            .route("/add-account", web::put().to(add_account_put_handler))
            .route("/delete-login", web::post().to(delete_account_post_handler))
            .route("/edit-login", web::post().to(edit_account_post_handler))
            .route("/delete-account", web::post().to(delete_user_post_handler))
            .route("/search", web::post().to(search_for_logins_post_handler))
    })
    .bind("127.0.0.1:7878")?
    .run()
    .await?;
    Ok(())
}

//the next block of functions is responsible for handling the routing of the requests
//ie call aproptiate helper functions, maintain bodydata and database connection

//PUT
async fn change_password_put_handler(body: web::Bytes, app_state: web::Data<AppState>) -> HttpResponse{
    println!("Received change password PUT request body: {:?}", body);
    let body_vec = &body.to_vec();
    let body_string = String::from_utf8_lossy(&body_vec);
    let conn = app_state.db_conn.clone();
    let conn = conn.lock().unwrap();
    let conn = &*conn;
    change_password(body_string.to_string(), conn)
}

async fn add_account_put_handler(body: web::Bytes, app_state: web::Data<AppState>) -> HttpResponse{
    println!("Received add account PUT request body: {:?}", body);
    let body_vec = &body.to_vec();
    let body_string = String::from_utf8_lossy(&body_vec);
    let conn = app_state.db_conn.clone();
    let conn = conn.lock().unwrap();
    let conn = &*conn;
    add_login(body_string.to_string(), conn)
}

//POST
async fn search_for_logins_post_handler(body: web::Bytes, app_state: web::Data<AppState>) -> HttpResponse{
    println!("Received search POST request body: {:?}", body);
    let body_vec = &body.to_vec();
    let body_string = String::from_utf8_lossy(&body_vec);
    let conn = app_state.db_conn.clone();
    let conn = conn.lock().unwrap();
    let conn = &*conn;
    get_search_results(body_string.to_string(), conn)
}

async fn main_post_handler(body: web::Bytes, app_state: web::Data<AppState>) -> HttpResponse{
    println!("Received main page POST request body: {:?}", body);
    let body_vec = &body.to_vec();
    let body_string = String::from_utf8_lossy(&body_vec);
    let conn = app_state.db_conn.clone();
    let conn = conn.lock().unwrap();
    let conn = &*conn;
    get_user_logins(body_string.to_string(), conn)
}

async fn forgot_password_post_handler(body: web::Bytes, app_state: web::Data<AppState>) ->HttpResponse{
    println!("Received forgot password POST request body: {:?}", body);
    let body_vec = &body.to_vec();
    let body_string = String::from_utf8_lossy(&body_vec);
    let conn = app_state.db_conn.clone();
    let conn = conn.lock().unwrap();
    let conn = &*conn;
    fp_is_account_in_database(body_string.to_string(), conn)
}

async fn signup_post_handler(body: web::Bytes, app_state: web::Data<AppState>) ->HttpResponse{
    println!("Received sign up POST request body: {:?}", body);
    let body_vec = &body.to_vec();
    let body_string = String::from_utf8_lossy(&body_vec);
    let conn = app_state.db_conn.clone();
    let conn = conn.lock().unwrap();
    let conn = &*conn;
    signup_new_user(body_string.to_string(), conn)
}

async fn login_post_handler(body: web::Bytes,  app_state: web::Data<AppState> ) ->HttpResponse{
    println!("Received login POST request body: {:?}", body);
    let body_vec = &body.to_vec();
    let body_string = String::from_utf8_lossy(&body_vec);
    let conn = app_state.db_conn.clone();
    let conn = conn.lock().unwrap();
    let conn = &*conn;
    login_registered_user(body_string.to_string(), conn)
}

async fn delete_account_post_handler(body: web::Bytes, app_state: web::Data<AppState>) -> HttpResponse{
    println!("Receieved delete login POST request body: {:?}", body);
    let body_vec = &body.to_vec();
    let body_string = String::from_utf8_lossy(&body_vec);
    let conn = app_state.db_conn.clone();
    let conn = conn.lock().unwrap();
    let conn = &*conn;
    delete_login(body_string.to_string(), conn)
}

async fn edit_account_post_handler(body: web::Bytes, app_state: web::Data<AppState>) -> HttpResponse {
    println!("Received edit POST request body: {:?}", body);
    let body_vec = &body.to_vec();
    let body_string = String::from_utf8_lossy(&body_vec);
    let conn = app_state.db_conn.clone();
    let conn = conn.lock().unwrap();
    let conn = &*conn;
    edit_login(body_string.to_string(), conn)
}

async fn delete_user_post_handler(body: web::Bytes, app_state: web::Data<AppState>) -> HttpResponse {
    println!("Received delete user POST request body: {:?}", body);
    let body_vec = &body.to_vec();
    let body_string = String::from_utf8_lossy(&body_vec);
    let conn = app_state.db_conn.clone();
    let conn = conn.lock().unwrap();
    let conn = &*conn;
    delete_user(body_string.to_string(), conn)
}

//functions ----------------------------------------------------------------------------------------------------------------------------------------------------------------

fn get_search_results(body: String, conn: &Connection) -> HttpResponse{
    let map = parse_serialized_data_into_hash_map(&body);
    let mut service_name:String = String::new();
    let mut account_nickname:String = String::new();
    let mut username:String = String::new();
    let mut url:String = String::new();
    let mut account_id:String = String::new();
    for(key, value) in map {
        match key.as_str() {
            "service_name" => service_name = value,
            "account_nickname" => account_nickname = value,
            "username" => username = value,
            "site_url" => url = value,
            "account_id" => account_id = value,
            _ => {}
        }
    } 
    if service_name.clone().ends_with("n/a"){
        service_name = String::new();
    }
    if account_nickname.clone().ends_with("n/a"){
        account_nickname = String::new();
    }
    if username.clone().ends_with("n/a"){
        username = String::new();
    }
    if url.clone().ends_with("n/a"){
        url = String::new();
    }
// Prepare SQL statement with placeholders for all parameters
let mut stmt = conn.prepare("
    SELECT Logins.ID, Logins.serviceName, Logins.accountNickname, Logins.userName, Logins.password, Logins.siteURL
    FROM Logins 
    WHERE Logins.accountID = ? AND Logins.serviceName LIKE ? AND Logins.accountNickname LIKE ? 
    AND Logins.userName LIKE ? AND Logins.siteURL LIKE ?;").unwrap();
    let id_param:i64 = account_id.parse().unwrap();
    let id_param: &dyn rusqlite::ToSql = &id_param;
    let service_name = format!("%{}%", service_name);
    let service_name_param: &dyn rusqlite::ToSql = &service_name;
    let account_nickname = format!("%{}%", account_nickname);
    let account_nickname_param: &dyn rusqlite::ToSql = &account_nickname;
    let username = format!("%{}%", username);
    let username_param: &dyn rusqlite::ToSql = &username;
    let site_url = format!("%{}%", url);
    let site_url_param: &dyn rusqlite::ToSql = &site_url;
    let login_iter = stmt.query_map(&[
        &id_param,
        &service_name_param,
        &account_nickname_param,
        &username_param,
        &site_url_param
    ], |row|{
        Ok(Login{
            id: row.get(0)?,
            service_name: row.get(1)?,
            account_nickname: row.get(2)?,
            username: row.get(3)?,
            password: row.get(4)?,
            site_url: row.get(5)?
        })
    });
// Iterate over login_iterator and handle each result
    let logins = login_iter;
    let logins = logins.expect("no logins");
    let logins: Result<Vec<Login>, RusqliteError>= logins.collect();
    let logins = logins.unwrap();
    if logins.len() == 0{
        return HttpResponse::NoContent().body("no content");
    }
    let mut response_body:String = String::new();
    for login in logins{
        response_body.push_str("service_name=");
        response_body.push_str(login.service_name.as_str());
        response_body.push_str("&account_nickname=");
        response_body.push_str(login.account_nickname.as_str());
        response_body.push_str("&username=");
        response_body.push_str(login.username.as_str());
        response_body.push_str("&password=");
        response_body.push_str(login.password.as_str());
        response_body.push_str("&site_url=");
        response_body.push_str(login.site_url.as_str());
        response_body.push_str(",");
        println!("{:?}", login);
    }
    HttpResponse::Ok().body(response_body.to_string())
}

//the next block of functions gets called by the previous block when a http request is made
//return an http response to be sent back to client side
fn get_user_logins(data: String, conn: &Connection) ->HttpResponse{
    let map = parse_serialized_data_into_hash_map(&data);
    let mut id: String = String::new();
    let mut username: String = String::new();
    let mut password: String = String::new();
    let mut email: String = String::new();
    let mut phone_number: String = String::new();
    for (key, value) in map {
        match key.as_str() {
            "id" => id = value,
            "username" => username = value,
            "password" => password = value,
            "email" => email = value,
            "phone_number" => phone_number = value,
            _ => {} 
        }
    }
    println!("Get Logins for user {} {} {} {} {}", id.clone(), username.clone(), password.clone(), email.clone(), phone_number.clone());
    let id_to_search_on: i64 = id.trim().parse().clone().unwrap();
    let mut stmt = conn.prepare("SELECT * FROM Logins WHERE Logins.accountID = ?1").unwrap();
    let login_iterator = stmt.query_map(&[&id_to_search_on],|row| {
        Ok(Login {
            id: row.get(0)?,
            service_name: row.get(1)?,
            account_nickname: row.get(2)?,
            username: row.get(3)?,
            password: row.get(4)?,
            site_url: row.get(5)?
        })
    });
    // Iterate over login_iterator and handle each result
    let logins = login_iterator;
    let logins = logins.expect("no logins");
    let logins: Result<Vec<Login>, RusqliteError>= logins.collect();
    let logins = logins.unwrap();
    if logins.len() == 0{
        return HttpResponse::NoContent().body("no content");
    }
    let mut response_body:String = String::new();
    for login in logins{
        response_body.push_str("service_name=");
        response_body.push_str(login.service_name.as_str());
        response_body.push_str("&account_nickname=");
        response_body.push_str(login.account_nickname.as_str());
        response_body.push_str("&username=");
        response_body.push_str(login.username.as_str());
        response_body.push_str("&password=");
        response_body.push_str(login.password.as_str());
        response_body.push_str("&site_url=");
        response_body.push_str(login.site_url.as_str());
        response_body.push_str(",");
    }
    HttpResponse::Ok().body(response_body.to_string())

}

fn login_registered_user(data: String, conn: &Connection) -> HttpResponse{
    let map = parse_serialized_data_into_hash_map(data.as_str());
    let mut username: String = String::new();
    let mut password: String = String::new();
    for (key, value) in map {
        match key.as_str() {
            "username" => username = value,
            "password" => password = value,
            _ => {} 
        }
    }
    let account = conn
    .query_row(
        "SELECT Accounts.accountID, Accounts.username, Accounts.password, Accounts.email, Accounts.phoneNumber
        FROM Accounts
        WHERE Accounts.username = ?1 AND Accounts.password = ?2",
        &[&username, &password],
        |row| {
            Ok(Account {
                id: row.get(0)?,
                username: row.get(1)?,
                password: row.get(2)?,
                email: row.get(3)?,
                phone_number: row.get(4)?
            })
        },
    );       
    match account {
            Ok(account) => {
                println!("Account: {:?}", account);
                let mut some_string = String::new();
                some_string.push_str("id=");
                let id_as_str: String = account.id.to_string();
                some_string.push_str(&id_as_str);
                some_string.push_str("&username=");
                some_string.push_str(&account.username);
                some_string.push_str("&password=");
                some_string.push_str(&account.password);
                some_string.push_str("&email=");
                some_string.push_str(&account.email);
                some_string.push_str("&phone_number=");
                some_string.push_str(&account.phone_number);
                return HttpResponse::Ok().body(some_string.to_string())
            }
            Err(rusqlite::Error::QueryReturnedNoRows) => {
                return HttpResponse::BadRequest().body("Failed login");
            }
            Err(err) => {
                println!("Error: {:?}", err);
                return HttpResponse::InternalServerError().body("Internal Server Error");
            }
        }
}

fn signup_new_user(data: String, conn: &Connection) -> HttpResponse{
    let map = parse_serialized_data_into_hash_map(data.as_str());
    let mut username: String = String::new();
    let mut phone_number: String = String::new();
    let mut email: String = String::new();
    let mut password: String = String::new();
    for (key, value) in map {
        match key.as_str() {
            "username" => username = value,
            "phone_number" => phone_number = value,
            "email" => email = value,
            "password" => password = value,
            _ => {} 
        }
    }
    println!("New Account to be Registered in Database: {} {} {} {}", username, password, email, phone_number);
    let result = conn.execute("INSERT INTO Accounts (username, password, email, phoneNumber) VALUES (?1, ?2, ?3, ?4)", [username.clone(), password.clone(), email.clone(), phone_number.clone()]);
    match result{
        Ok(_)=>{
            let account = conn
            .query_row(
                "SELECT Accounts.accountID, Accounts.username, Accounts.password, Accounts.email, Accounts.phoneNumber
                FROM Accounts
                WHERE Accounts.username = ?1 AND Accounts.password = ?2",
                &[&username, &password],
                |row| {
                    Ok(Account {
                        id: row.get(0)?,
                        username: row.get(1)?,
                        password: row.get(2)?,
                        email: row.get(3)?,
                        phone_number: row.get(4)?
                    })
                },
            );       
            match account {
                    Ok(account) => {
                        println!("Account: {:?}", account);
                        let mut some_string = String::new();
                        some_string.push_str("id=");
                        let id_as_str: String = account.id.to_string();
                        some_string.push_str(&id_as_str);
                        some_string.push_str("&username=");
                        some_string.push_str(&account.username);
                        some_string.push_str("&password=");
                        some_string.push_str(&account.password);
                        some_string.push_str("&email=");
                        some_string.push_str(&account.email);
                        some_string.push_str("&phone_number=");
                        some_string.push_str(&account.phone_number);
                        return HttpResponse::Ok().body(some_string.to_string())
                    }
                    Err(rusqlite::Error::QueryReturnedNoRows) => {
                        return HttpResponse::BadRequest().body("Failed login");
                    }
                    Err(err) => {
                        println!("Error: {:?}", err);
                        return HttpResponse::InternalServerError().body("Internal Server Error");
                    }
                }
        }
        Err(_)=>{
            return HttpResponse::NotModified().body("User could not be added to the database");
        }
    }
}

fn add_login(data:String, conn:&Connection) -> HttpResponse{
    let map = parse_serialized_data_into_hash_map(data.as_str());
    let mut service_name:String = String::new();
    let mut account_nickname:String = String::new();
    let mut username:String = String::new();
    let mut password:String = String::new();
    let mut url:String = String::new();
    let mut account_id:String = String::new();
    for(key, value) in map {
        match key.as_str() {
            "service_name" => service_name = value,
            "account_nickname" => account_nickname = value,
            "username" => username = value,
            "password" => password = value,
            "site_url" => url = value,
            "account_id" => account_id = value,
            _ => {}
        }
    }
    let result = conn.execute("INSERT INTO Logins (serviceName, accountNickname, userName, password, siteURL, accountID) VALUES (?1, ?2, ?3, ?4, ?5, ?6)",
    [service_name.clone(), account_nickname.clone(), username.clone(), password.clone(), url.clone(), account_id.clone()]);
    if result.unwrap() > 0 {
        return HttpResponse::Ok().body("");
    } else {
        return HttpResponse::BadRequest().body("");
    }
}

fn delete_login(data: String, conn:&Connection) -> HttpResponse {
    let map = parse_serialized_data_into_hash_map(data.as_str());
    let mut service_name:String = String::new();
    let mut account_nickname:String = String::new();
    let mut username:String = String::new();
    let mut password:String = String::new();
    let mut url:String = String::new();
    let mut account_id:String = String::new();
    for(key, value) in map {
        match key.as_str() {
            "service_name" => service_name = value,
            "account_nickname" => account_nickname = value,
            "username" => username = value,
            "password" => password = value,
            "site_url" => url = value,
            "account_id" => account_id = value,
            _ => {}
        }
    } 
    let result = conn.execute("DELETE FROM Logins 
        WHERE serviceName = ?1 AND accountNickname = ?2 AND userName = ?3 AND password = ?4 AND siteURL = ?5 AND accountID = ?6", 
        [service_name.clone(), account_nickname.clone(), username.clone(), password.clone(), url.clone(), account_id.clone()]);
    if result.unwrap() > 0 {
        return HttpResponse::Ok().body("");
    } else {
        return HttpResponse::BadRequest().body("");
    }
}

fn edit_login(data: String, conn:&Connection) -> HttpResponse {
    let map = parse_serialized_data_into_hash_map(data.as_str());
    let mut service_name:String = String::new();
    let mut account_nickname:String = String::new();
    let mut username:String = String::new();
    let mut password:String = String::new();
    let mut url:String = String::new();
    let mut account_id:String = String::new();
    let mut new_service_name:String = String::new();
    let mut new_account_nickname:String = String::new();
    let mut new_username:String = String::new();
    let mut new_password:String = String::new();
    let mut new_url:String = String::new();
    for(key, value) in map {
        match key.as_str() {
            "service_name" => service_name = value,
            "account_nickname" => account_nickname = value,
            "username" => username = value,
            "password" => password = value,
            "site_url" => url = value,
            "account_id" => account_id = value,
            "new_service_name" => new_service_name = value,
            "new_account_nickname" => new_account_nickname = value,
            "new_username" => new_username = value,
            "new_password" => new_password = value,
            "new_site_url" => new_url = value,
            _ => {}
        }
    } 
    let result = conn.execute("UPDATE Logins SET serviceName = ?1, accountNickname = ?2, userName = ?3, password = ?4, siteURL = ?5
        WHERE serviceName = ?6 AND accountNickname = ?7 AND userName = ?8 AND password = ?9 AND siteURL = ?10 AND accountID = ?11",
        [new_service_name.clone(), new_account_nickname.clone(), new_username.clone(), new_password.clone(), new_url.clone(),
        service_name.clone(), account_nickname.clone(), username.clone(), password.clone(), url.clone(), account_id.clone()]);
    if result.unwrap() > 0 {
         return HttpResponse::Ok().body("");
    } else {
        return HttpResponse::BadRequest().body("");
    }
}

fn delete_user(data: String, conn:&Connection) -> HttpResponse {
    let map = parse_serialized_data_into_hash_map(data.as_str());
    let mut account_id:String = String::new();
    for(key, value) in map {
        match key.as_str() {
            "account_id" => account_id = value,
            _ => {}
        }
    }
    let mut result = conn.execute("DELETE FROM Logins WHERE accountID = ?1", [account_id.clone()]);
    if result.expect("bad_result").clone() > 0 {
        result = conn.execute("DELETE FROM Accounts WHERE accountID = ?1", [account_id.clone()]);
        if result.unwrap() > 0 {
            return HttpResponse::Ok().body("");
        } else {
            return HttpResponse::BadRequest().body("");
        }
    } else {
        return HttpResponse::BadRequest().body("");
    }
}

fn change_password(body: String, conn: &Connection) -> HttpResponse{
    println!("{:?}", body.to_string());
    let map = parse_serialized_data_into_hash_map(&body.to_string());
    let mut id: String = String::new();
    let mut password: String = String::new();
    for (key, value) in map{
        match key.as_str() {
            "id" =>id = value, 
            "new_password" => password = value,
            _ => {}   
        }
    }
    let id_param: i64 = id.parse().unwrap();
    let id_param: &dyn rusqlite::ToSql = &id_param;
    let password_param: &dyn rusqlite::ToSql = &password;
    let result = conn.execute(
        "UPDATE Accounts
         SET password = ?
         WHERE accountID = ?",
        params![&password_param, &id_param],
    );
    if result.unwrap() > 0 {
        return HttpResponse::Ok().body("");
   } else {
       return HttpResponse::BadRequest().body("");
   }
}

//helper functions ------------------------------------------------------------------------------------------------------------------------------------------------------------

fn fp_is_account_in_database(body: String, conn: &Connection) -> HttpResponse{
    let map = parse_serialized_data_into_hash_map(&body);
    let mut username: String = String::new();
    let mut email: String = String::new();
    let mut phone: String = String::new();
    for (key, value) in map{
        match key.as_str() {
            "username" => username = value,
            "email" => email = value,
            "phone_number" => phone = value,
            _ => {}
        }
    }
    let result = conn.query_row("
    SELECT *
    FROM Accounts
    WHERE Accounts.username = ?
    AND
    (Accounts.email = ?
    OR
    Accounts.phoneNumber = ?);", &[
        &username.as_str(),
        &email.as_str(),
        &phone.as_str()], |row|{
            Ok(Account{
                id: row.get(0)?,
                username: row.get(1)?,
                password: row.get(2)?,
                email: row.get(3)?,
                phone_number: row.get(4)?,
            })
        });
    match result{
        Ok(account)=>{
            let mut response_body: String = String::new();
            response_body.push_str(format!("id={}", account.id).as_str());
            return HttpResponse::Ok().body(response_body);
        }
        Err(RusqliteError::QueryReturnedNoRows) =>{
            return HttpResponse::NoContent().body("Sorry couldnt find your account");
        }
        Err(_rusqlite_error) =>{
            return HttpResponse::ExpectationFailed().body("");
        }
    }
}

fn parse_serialized_data_into_hash_map(data: &str) -> HashMap<String, String>{
    let mut map = HashMap::new();
    // Split the input string by '&' to get key-value pairs
    for pair in data.split('&') {
        // Split each key-value pair by '='
        let mut iter = pair.split('=');
        if let (Some(key), Some(value)) = (iter.next(), iter.next()) {
            // Insert key-value pair into the HashMap
            map.insert(key.to_string(), value.to_string());
        }
    }
    map
}