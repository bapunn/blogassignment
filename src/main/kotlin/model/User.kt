package model

import jakarta.persistence.*

@Entity
class User {
    // 사용자의 고유 식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    // 사용자의 이메일 형식의 유일한 식별자
    @Column(nullable = false, unique = true)
    private var username: String

    // 사용자의 닉네임
    @Column(nullable = false, unique = true)
    private var nickname: String

    // 사용자의 비밀번호
    @Column(nullable = false)
    private var password: String

    // 소셜 로그인을 위한 식별자 (예: 구글, 페이스북 아이디)
    @Column(unique = true)
    private var socialId: String?

    // 생성자를 통한 초기화
    constructor(username: String, nickname: String, password: String) {
        this.username = username
        this.nickname = nickname
        this.password = password

        // 소셜 아이디는 초기에는 null로 설정
        socialId = null
    }
}
