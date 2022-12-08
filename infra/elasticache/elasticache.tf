#### ref : https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_cluster#redis-cluster-mode-disabled-read-replica-instance
# create vpc
resource "aws_vpc" "infra_test_vpc" {
  cidr_block = "10.1.0.0/16"
}

# create subnet
resource "aws_subnet" "infra_test_vpc_subnet" {
  vpc_id     = aws_vpc.infra_test_vpc.id
  cidr_block = "10.1.1.0/24"

  tags = {
    Name = "infra-test-subnet"
  }
}

# elasticache에 subnet group 연결
resource "aws_elasticache_subnet_group" "infra_test_vpc_subnet_group" {
  name       = "test-cache-subnet"
  subnet_ids = [aws_subnet.infra_test_vpc_subnet.id]
}

# create elasticache (redis)
resource "aws_elasticache_cluster" "redis-terraform-test" {
  cluster_id           = "redis-terraform-test"
  engine               = "redis"
  node_type            = "cache.t2.micro"
  num_cache_nodes      = 1 # node 1개
  parameter_group_name = "default.redis7" # 파라미터 그룹
  engine_version       = "7.0" # 레디스 버전
  port                 = 6379

  subnet_group_name    = aws_elasticache_subnet_group.infra_test_vpc_subnet_group.name
}

#### replication group 디테일한 설정하려면 : https://registry.terraform.io/providers/hashicorp/aws/latest/docs/resources/elasticache_replication_group
#### 미구현
