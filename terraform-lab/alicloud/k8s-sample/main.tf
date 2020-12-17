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
      ports = "2041"
      protocol = "tcp"
      priority = 1
      cidr_blocks = "0.0.0.0/0"
    },
  ]
}

resource "alicloud_key_pair" "basic" {
  key_name = "demo-key-pair"
}

variable "name_prefix" { default = "demo-k8s" }
variable "password" { default = "Abc12345" }
variable "key_name" { default = "demo-key-pair" }
variable "pod_cidr" { default = "172.20.0.0/20" }
variable "service_cidr" { default = "172.21.0.0/20" }
variable "worker_number" { default = 2 }
variable "worker_instance_types" { default = ["ecs.n4.xlarge"] }
variable "cluster_addons" {
  description = "Addon components in kubernetes cluster"

  type = list(object({
    name      = string
    config    = string
  }))

  default = [
    {
      name     = "flannel",
      config   = "",
    },
    {
      name     = "flexvolume",
      config   = "",
    },
    {
      name     = "alicloud-disk-controller",
      config   = "",
    },
    {
      name     = "logtail-ds",
      config   = "{IngressDashboardEnabled:true}",
    },
    {
      name     = "nginx-ingress-controller",
      config   = "{IngressSlbNetworkType:internet}",
    },
  ]
}

resource "alicloud_cs_managed_kubernetes" "k8s" {
  name_prefix = var.name_prefix
//  password = var.password
  key_name = var.key_name
  pod_cidr = var.pod_cidr
  service_cidr = var.service_cidr
  worker_number = var.worker_number
  worker_vswitch_ids = [module.vpc.this_vswitch_ids[0], module.vpc.this_vswitch_ids[1]]
  worker_instance_types = var.worker_instance_types
  dynamic "addons" {
    for_each = var.cluster_addons
    content {
      name                    = lookup(addons.value, "name", var.cluster_addons)
      config                  = lookup(addons.value, "config", var.cluster_addons)
    }
  }
}
