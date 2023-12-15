package controller

import dto.requestDto.SignupRequestDto
import service.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequiredArgsConstructor
class UserController {
    private val userService: UserService? = null
    private val s3Service: S3Service? = null

    //회원가입
    @PostMapping("/api/signup")
    fun signupUser(
        @RequestPart("signup") requestDto: SignupRequestDto?,
        @RequestPart("profileImage") profileImages: List<MultipartFile>
    ): ResponseEntity {
    //username 중복체크
    @PostMapping("/api/signup/checkID")
    fun checkUsername(@RequestBody requestDto: SignupRequestDto?): ResponseEntity {
        return userService.checkUsername(requestDto)
    }

    //nickname 중복체크
    @PostMapping("/api/signup/nickID")
    fun checkNickname(@RequestBody requestDto: SignupRequestDto?): ResponseEntity {
        return userService.checkNickname(requestDto)
    }

    //소셜로그인 사용자 정보 조회
    @GetMapping("/social/user/islogin")
    fun socialUserInfo(@AuthenticationPrincipal userDetails: UserDetailsImpl?): ResponseEntity {
        return userService.socialUserInfo(userDetails)
    }
}