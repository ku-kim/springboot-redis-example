#### create iam user
resource "aws_iam_user" "infra-users" {
  count = length(var.users)
  name = element(var.users, count.index)
  path = "/infra/"

  tags = {
    Name = "infra-developers"
  }
}

#### -------------------------------------
#### create iam group
resource "aws_iam_group" "infra_group" {
  name = "infra_Group"
  path = "/users/"
}

resource "aws_iam_group_policy_attachment" "infra_group_policy_attach" {
  #name = aws_iam_group.be_group.name

  group = aws_iam_group.infra_group.name

  policy_arn = "arn:aws:iam::aws:policy/ReadOnlyAccess"
}


resource "aws_iam_user_group_membership" "infra_group_membership" {
  count = length(var.users)

  user = element(var.users, count.index)

  groups = [
    aws_iam_group.infra_group.name
  ]
}

#### -------------------------------------
#### add iam policy
resource "aws_iam_user_policy" "art_devops_black" {
  name = "s3_images_access"
  count = length(var.users)
  user = aws_iam_user.infra-users[count.index].name

  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": "elasticache:*",
            "Effect": "Allow",
            "Resource": "*"
        },
        {
            "Action": "iam:CreateServiceLinkedRole",
            "Effect": "Allow",
            "Resource": "arn:aws:iam::*:role/aws-service-role/elasticache.amazonaws.com/AWSServiceRoleForElastiCache",
            "Condition": {
                "StringLike": {
                    "iam:AWSServiceName": "elasticache.amazonaws.com"
                }
            }
        }
    ]
}
EOF
}
