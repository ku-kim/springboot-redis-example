# ecs에서 사용할 보안그룹
resource "aws_security_group" "infra_test_redis_security_group" {
  name = "infra-test-redis-security-group"

  vpc_id = aws_vpc.infra_test_vpc.id

  ingress {
    from_port   = 6379
    to_port     = 6379
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 65535
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }


  tags = {
    Name = "infra"
  }
}
