package service

import com.sparta.coffang.dto.requestDto.AdminRequestDto
import java.util.function.Supplier
import java.util.regex.Pattern

@Service
@RequiredArgsConstructor
class UserService {
    private val userRepository: UserRepository? = null
    private val passwordEncoder: PasswordEncoder? = null

    @Value("\${spring.admin.token}")
    var ADMIN_TOKEN: String? = null
    fun signupUser(requestDto: SignupRequestDto, image: String): ResponseEntity {
        val passwordPattern = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,10}"
        val nicknamePattern = "^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣~!@#$%^&*]{2,8}" //닉네임 정규식 패턴
        val username: String = requestDto.getUsername()
        val nickname: String = requestDto.getNickname()
        var password: String = requestDto.getPassword()


        //nickname 정규식 맞지 않는 경우 오류메시지 전달
        if (nickname == "") throw CustomException(ErrorCode.EMPTY_NICKNAME) else if (userRepository.findByNickname(
                nickname
            ).isPresent()
        ) throw CustomException(ErrorCode.DUPLICATE_NICKNAME) else if (2 > nickname.length || 8 < nickname.length) throw CustomException(
            ErrorCode.NICKNAME_LEGNTH
        ) else if (!Pattern.matches(nicknamePattern, nickname)) throw CustomException(ErrorCode.NICKNAME_WRONG)

        //password 정규식 맞지 않는 경우 오류메시지 전달
        if (password == "") throw CustomException(ErrorCode.EMPTY_PASSWORD) else if (8 > password.length || 20 < password.length) throw CustomException(
            ErrorCode.PASSWORD_LEGNTH
        ) else if (!Pattern.matches(passwordPattern, password)) throw CustomException(ErrorCode.PASSWORD_WRONG)
        password = passwordEncoder.encode(requestDto.getPassword()) // 패스워드 암호화
        val user = User(username, nickname, password, image)
        userRepository.save(user)
        return ResponseEntity("회원가입을 축하합니다", HttpStatus.OK)
    }

    //username 중복체크
    fun checkUsername(requestDto: SignupRequestDto): ResponseEntity {
        val username: String = requestDto.getUsername()
        val emailPattern = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$" //이메일 정규식 패턴

        //username 정규식 맞지 않는 경우 오류메시지 전달
        if (username == "") throw CustomException(ErrorCode.EMPTY_USERNAME) else if (!Pattern.matches(
                emailPattern,
                username
            )
        ) throw CustomException(ErrorCode.USERNAME_WRONG) else if (userRepository.findByUsername(username)
                .isPresent()
        ) throw CustomException(ErrorCode.DUPLICATE_EMAIL)
        return ResponseEntity("사용 가능한 이메일입니다.", HttpStatus.OK)
    }

    //nickname 중복체크
    fun checkNickname(requestDto: SignupRequestDto): ResponseEntity {
        val nickname: String = requestDto.getNickname()
        val nicknamePattern = "^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣~!@#$%^&*]{2,8}" //닉네임 정규식 패턴

        //nickname 정규식 맞지 않는 경우 오류메시지 전달
        if (nickname == "") throw CustomException(ErrorCode.EMPTY_NICKNAME) else if (userRepository.findByNickname(
                nickname
            ).isPresent()
        ) throw CustomException(ErrorCode.DUPLICATE_NICKNAME) else if (2 > nickname.length || 8 < nickname.length) throw CustomException(
            ErrorCode.NICKNAME_LEGNTH
        ) else if (!Pattern.matches(nicknamePattern, nickname)) throw CustomException(ErrorCode.NICKNAME_WRONG)
        return ResponseEntity("사용 가능한 닉네임입니다.", HttpStatus.OK)
    }

    //로그인 후 관리자 권한 얻을 수 있는 API
    fun adminAuthorization(requestDto: AdminRequestDto, userDetails: UserDetailsImpl): ResponseEntity {
        // 사용자 ROLE 확인
        var role: UserRoleEnum = UserRoleEnum.USER
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw CustomException(ErrorCode.INVALID_AUTHORITY_WRONG) // 토큰값이 틀림
            }
            role = UserRoleEnum.ADMIN
        }

        //역할 변경
        userDetails.getUser().setRole(role)
        //변경된 역할 저장
        userRepository.save(userDetails.getUser())
        return ResponseEntity("관리자 권한으로 변경되었습니다", HttpStatus.OK)
    }

    //소셜로그인 사용자 정보 조회
    fun socialUserInfo(userDetails: UserDetailsImpl): ResponseEntity {
        //로그인 한 user 정보 검색
        val user: User =
            userRepository.findBySocialId(userDetails.getUser().getSocialId()).orElseThrow<RuntimeException>(
                Supplier<RuntimeException> { CustomException(ErrorCode.USER_NOT_FOUND) }
            )

        //찾은 user엔티티를 dto로 변환해서 반환하기
        val socialLoginResponseDto = SocialLoginResponseDto(user, true)
        return ResponseEntity.ok().body(socialLoginResponseDto)
    }

    //회원가입에 이미지가 null이 들어올 때
    fun signupNullUser(requestDto: SignupImgRequestDto): ResponseEntity {
        val passwordPattern = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,10}" //영어, 숫자 4자이상 10이하
        val nicknamePattern = "^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣~!@#$%^&*]{2,8}" //닉네임 정규식 패턴
        val username: String = requestDto.getUsername()
        val nickname: String = requestDto.getNickname()
        var password: String = requestDto.getPassword()


        //nickname 정규식 맞지 않는 경우 오류메시지 전달
        if (nickname == "") throw CustomException(ErrorCode.EMPTY_NICKNAME) else if (userRepository.findByNickname(
                nickname
            ).isPresent()
        ) throw CustomException(ErrorCode.DUPLICATE_NICKNAME) else if (2 > nickname.length || 8 < nickname.length) throw CustomException(
            ErrorCode.NICKNAME_LEGNTH
        ) else if (!Pattern.matches(nicknamePattern, nickname)) throw CustomException(ErrorCode.NICKNAME_WRONG)

        //password 정규식 맞지 않는 경우 오류메시지 전달
        if (password == "") throw CustomException(ErrorCode.EMPTY_PASSWORD) else if (8 > password.length || 20 < password.length) throw CustomException(
            ErrorCode.PASSWORD_LEGNTH
        ) else if (!Pattern.matches(passwordPattern, password)) throw CustomException(ErrorCode.PASSWORD_WRONG)
        password = passwordEncoder.encode(requestDto.getPassword()) // 패스워드 암호화
        val user = User(username, nickname, password, profileImage)
        userRepository.save(user)
        return ResponseEntity("회원가입을 축하합니다", HttpStatus.OK)
    }
}