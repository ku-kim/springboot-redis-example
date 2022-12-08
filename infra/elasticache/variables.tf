# iam 유저 생성 목록
variable "users" {
  description = "Create IAM Users"

  type        = list

  default     = ["infra-kukim"] # n명의 유저를 생성하려면 리스트에 데이터를 추가하세요
  # default     = ["infra-user1", "infra-user2", ...]
}
