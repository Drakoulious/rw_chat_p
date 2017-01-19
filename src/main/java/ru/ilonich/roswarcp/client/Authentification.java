package ru.ilonich.roswarcp.client;

/**
 * Created by Никола on 13.01.2017.
 */
public class Authentification {
    private String login;
    private String password;
    //запрос при входе первый раз на сайт
/*  GET / HTTP/1.1
    Host: www.roswar.ru
    Connection: keep-alive
    Upgrade-Insecure-Requests: 1
    User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36
    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,**//*//*;q=0.8
    Accept-Encoding: gzip, deflate, sdch
    Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4
    If-Modified-Since: Thu, 19 Jan 2017 02:54:26 GMT
*/
    //ответ
/*  HTTP/1.1 200 OK
    Server: nginx/1.2.3
    Date: Thu, 19 Jan 2017 02:55:56 GMT
    Content-Type: text/html
    Transfer-Encoding: chunked
    Connection: close
    X-Powered-By: PHP/5.3.10-1ubuntu3.6
    Set-Cookie: PHPSESSID=k5f4p8r6tthp960ckuums4lvc3; path=/; domain=.roswar.ru
    Expires: Mon, 26 Jul 1997 05:00:00 GMT
    Last-Modified: Thu, 19 Jan 2017 02:55:56 GMT
    Cache-Control: no-cache, must-revalidate
    Pragma: no-cache
    P3P: CP="IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT"
    Set-Cookie: authkey=deleted; expires=Thu, 01-Jan-1970 00:00:01 GMT; path=/; domain=.roswar.ru
    Set-Cookie: userid=deleted; expires=Thu, 01-Jan-1970 00:00:01 GMT; path=/; domain=.roswar.ru
    Content-Encoding: gzip
*/
    //логин
/*  POST / HTTP/1.1
    Host: www.roswar.ru
    Connection: keep-alive
    Content-Length: 55
    Cache-Control: max-age=0
    Origin: http://www.roswar.ru
    Upgrade-Insecure-Requests: 1
    User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36
    Content-Type: application/x-www-form-urlencoded
    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*//*;q=0.8
    Referer: http://www.roswar.ru/
    Accept-Encoding: gzip, deflate
    Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4
    Cookie: PHPSESSID=k5f4p8r6tthp960ckuums4lvc3; _ym_uid=1484790765808493940; _ym_isad=2; _ym_visorc_22477357=w
*/
    //параметры пересылаемые при логине
    // action=login&email=XXX&password=XXX

    //ответ на логин
/*  HTTP/1.1 302 Moved Temporarily
    Server: nginx/1.2.3
    Date: Thu, 19 Jan 2017 03:09:48 GMT
    Content-Type: text/html
    Transfer-Encoding: chunked
    Connection: close
    X-Powered-By: PHP/5.3.10-1ubuntu3.6
    Expires: Mon, 26 Jul 1997 05:00:00 GMT
    Last-Modified: Thu, 19 Jan 2017 03:09:48 GMT
    Cache-Control: no-cache, must-revalidate
    Pragma: no-cache
    P3P: CP="IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT"
    Set-Cookie: authkey=deleted; expires=Thu, 01-Jan-1970 00:00:01 GMT; path=/; domain=.roswar.ru
    Set-Cookie: userid=deleted; expires=Thu, 01-Jan-1970 00:00:01 GMT; path=/; domain=.roswar.ru
    Set-Cookie: authkey=XXX; expires=Sat, 08-Mar-2064 06:19:36 GMT; path=/; domain=.roswar.ru
    Set-Cookie: userid=XXX; expires=Sat, 08-Mar-2064 06:19:36 GMT; path=/; domain=.roswar.ru
    Set-Cookie: player=XXX; expires=Sat, 08-Mar-2064 06:19:36 GMT; path=/; domain=.roswar.ru
    Set-Cookie: player_id=XXX; expires=Sat, 08-Mar-2064 06:19:36 GMT; path=/; domain=.roswar.ru
    Location: /player/#login
*/
}
