terraform {
  required_version = ">= 0.12"
}

provider "alicloud" {
  access_key = var.access_key
  secret_key = var.secret_key
  region = "cn-hongkong"
}

module "vpc" {
  source = "alibaba/vpc/alicloud"
  region = "cn-hongkong"
  profile = "default"

  vpc_name = "demo-vpc"
  vpc_cidr = "192.168.0.0/16"

  availability_zones = ["cn-hongkong-b", "cn-hongkong-c"]
  vswitch_cidrs = ["192.168.1.0/24", "192.168.2.0/24"]
}

module "sg" {
  source = "alibaba/security-group/alicloud"
  region = "cn-hongkong"
  profile = "default"

  name = "demo-sg"
  vpc_id = module.vpc.this_vpc_id

  ingress_with_cidr_blocks_and_ports = [
    {
      ports = "22"
      protocol = "tcp"
      priority = 1
      cidr_blocks = "0.0.0.0/0"
    },
    {
      ports = "2041"
      protocol = "tcp"
      priority = 2
      cidr_blocks = "0.0.0.0/0"
    },
  ]
}

data "alicloud_images" "ubuntu" {
  most_recent = true
  name_regex  = "^ubuntu_18.*64"
}

module "ecs_cluster" {
  source  = "alibaba/ecs-instance/alicloud"
  profile = "default"
  region  = "cn-hongkong"

  number_of_instances = 2

  name                        = "demo-cluster"
  use_num_suffix              = true
  image_id                    = data.alicloud_images.ubuntu.ids.0
  instance_type               = "ecs.sn1ne.large"
  vswitch_id                  = module.vpc.this_vswitch_ids[0]
  security_group_ids          = [module.sg.this_security_group_id]
  associate_public_ip_address = true
  internet_max_bandwidth_out  = 10

  key_name = "demo-key-pair"

  system_disk_category = "cloud_ssd"
  system_disk_size     = 50

  tags = {
    Created      = "Terraform"
    Environment = "dev"
  }
}
