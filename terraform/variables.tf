variable "aws_region" {
  description = "La région AWS"
  type        = string
  default     = "us-east-1"
}

variable "cluster_name" {
  description = "Nom du cluster EKS"
  type        = string
  default     = "my_new_cluster"
}

variable "subnet_ids" {
  description = "IDs des sous-réseaux pour EKS"
  type        = list(string)
  default = [
    "subnet-0a17c215f65b17b80", # us-east-1a
    "subnet-088dc9ec627a525c4", # us-east-1b
  ]
}

variable "role_arn" {
  description = "ARN du rôle IAM pour EKS"
  type        = string
  default     = "arn:aws:iam::665416441750:role/LabRole"
}

variable "vpc_id" {
  description = "L'ID du VPC pour le cluster EKS"
  type        = string
  default     = "vpc-089e1434497448ec4"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "172.31.0.0/16"
}
