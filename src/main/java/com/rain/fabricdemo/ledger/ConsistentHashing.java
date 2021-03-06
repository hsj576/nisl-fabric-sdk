package com.rain.fabricdemo.ledger;

import java.util.*;

    public class ConsistentHashing {
        // 物理节点
        private Set<String> physicalNodes = new TreeSet<String>() {
            {
                add("smallbank");
                add("smallbank_1_shard");
                add("smallbank_2_shard");
                add("smallbank_3_shard");
                add("smallbank_4_shard");
                add("smallbank_5_shard");
                add("smallbank_6_shard");
                add("smallbank_7_shard");
                add("smallbank_8_shard");
                add("smallbank_9_shard");
                add("smallbank_10_shard");
                add("smallbank_11_shard");
                add("smallbank_12_shard");
                add("smallbank_13_shard");
                add("smallbank_14_shard");
                add("smallbank_15_shard");
            }
        };
        //虚拟节点
        private final int VIRTUAL_COPIES = 50000; // 物理节点至虚拟节点的复制倍数
        private TreeMap<Long, String> virtualNodes = new TreeMap<>(); // 哈希值 => 物理节点
        // 32位的 Fowler-Noll-Vo 哈希算法
        // https://en.wikipedia.org/wiki/Fowler–Noll–Vo_hash_function
        private static Long FNVHash(String key) {
            final int p = 16777619;
            Long hash = 2166136261L;
            for (int idx = 0, num = key.length(); idx < num; ++idx) {
                hash = (hash ^ key.charAt(idx)) * p;
            }
            hash += hash << 13;
            hash ^= hash >> 7;
            hash += hash << 3;
            hash ^= hash >> 17;
            hash += hash << 5;

            if (hash < 0) {
                hash = Math.abs(hash);
            }
            return hash;
        }
        // 根据物理节点，构建虚拟节点映射表
        public ConsistentHashing() {
            for (String nodeIp : physicalNodes) {
                addPhysicalNode(nodeIp);
            }
        }
        // 添加物理节点
        public void addPhysicalNode(String nodeIp) {
            for (int idx = 0; idx < VIRTUAL_COPIES; ++idx) {
                long hash = FNVHash(nodeIp + "#" + idx);
                virtualNodes.put(hash, nodeIp);
            }
        }
        // 删除物理节点
        public void removePhysicalNode(String nodeIp) {
            for (int idx = 0; idx < VIRTUAL_COPIES; ++idx) {
                long hash = FNVHash(nodeIp + "#" + idx);
                virtualNodes.remove(hash);
            }
        }
        // 查找对象映射的节点
        public String getServer(String object) {
            long hash = FNVHash(object);
            SortedMap<Long, String> tailMap = virtualNodes.tailMap(hash); // 所有大于 hash 的节点
            Long key = tailMap.isEmpty() ? virtualNodes.firstKey() : tailMap.firstKey();
            return virtualNodes.get(key);
        }
        // 统计对象与节点的映射关系
        public void dumpObjectNodeMap(String label, int objectMin, int objectMax) {
            // 统计
            Map<String, Integer> objectNodeMap = new TreeMap<>(); // IP => COUNT
            for (int object = objectMin; object <= objectMax; ++object) {
                String nodeIp = getServer(Integer.toString(object));
                Integer count = objectNodeMap.get(nodeIp);
                objectNodeMap.put(nodeIp, (count == null ? 0 : count + 1));
            }
            // 打印
            double totalCount = objectMax - objectMin + 1;
            System.out.println("======== " + label + " ========");
            for (Map.Entry<String, Integer> entry : objectNodeMap.entrySet()) {
                long percent = (int) (100 * entry.getValue() / totalCount);
                System.out.println("IP=" + entry.getKey() + ": RATE=" + percent + "%");
            }
        }

//        public static void main(String[] args) {
//            ConsistentHashing ch = new ConsistentHashing();
//            for(int i=1;i<10;i++){
//
//                String server = ch.getServer(String.valueOf(i));
//                System.out.println("test_sample_"+i+" is routed to "+server);
//            }
//
//            // 初始情况
//            ch.dumpObjectNodeMap("Information", 0, 65536);
//
//
//            // 添加物理节点
//           // ch.addPhysicalNode("abstore_4_shard");
//            //ch.dumpObjectNodeMap("添加物理节点", 0, 65536);
//        }

    }

