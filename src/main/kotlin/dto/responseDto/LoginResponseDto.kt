package dto.responseDto

class LoginResponseDto//login true/ false 상황     //일반 로그인할 때 프론트에 내려주는 값
    (//로그인 시 body로 내려가는 사용자 정보
    private val userId: Long,
    private val nickname: String,
    private val login: Boolean,
    private val accessToken: String,
    private val profileImage: String
)