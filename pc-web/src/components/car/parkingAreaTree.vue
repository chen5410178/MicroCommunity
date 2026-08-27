<template>
  <div class="bg-white margin-top-xs padding border-radius tree-div">
    <el-tree
      v-if="treeData.length > 0"
      :data="treeData"
      :props="defaultProps"
      node-key="id"
      :expand-on-click-node="false"
      @node-click="handleNodeClick"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <i :class="data.icon" style="margin-right:5px"></i>
          <span>{{ node.label }}</span>
        </span>
      </template>
    </el-tree>
    <div v-else class="no-data">
      {{ $t('parkingAreaTree.noData') }}
    </div>
  </div>
</template>

<script>
import { queryParkingAreaAndSections } from '@/api/car/listParkingSpaceApi'

export default {
  name: 'ParkingAreaTree',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    refreshTree() {
      this.loadData()
    },
    async loadData() {
      try {
        const response = await queryParkingAreaAndSections()
        this.treeData = this.transformData(response.data)
      } catch (error) {
        console.error('加载停车场区域树失败', error)
      }
    },
    transformData(list) {
      const treeData = []
      const paMap = {}

      list.forEach(item => {
        if (!paMap[item.paId]) {
          paMap[item.paId] = {
            id: `pa_${item.paId}`,
            paId: item.paId,
            num: item.num,
            icon: 'el-icon-office-building',
            text: item.num,
            children: []
          }
          treeData.push(paMap[item.paId])
        }

        if (item.sectionId) {
          paMap[item.paId].children.push({
            id: `sec_${item.sectionId}`,
            sectionId: item.sectionId,
            paId: item.paId,
            sectionNum: item.sectionNum,
            sectionName: item.sectionName,
            text: item.sectionName || item.sectionNum,
            icon: 'el-icon-map-location'
          })
        }
      })

      return treeData
    },
    handleNodeClick(data) {
      if (data.id.startsWith('pa_')) {
        this.$emit('switchParkingAreaSection', {
          paId: data.paId,
          sectionId: '',
          num: data.num,
          type: 'pa'
        })
      } else {
        this.$emit('switchParkingAreaSection', {
          paId: data.paId,
          sectionId: data.sectionId,
          sectionNum: data.sectionNum,
          sectionName: data.sectionName,
          paName: data.num,
          num: data.num,
          type: 'section'
        })
      }
    }
  }
}
</script>

<style scoped>
.tree-div {
  min-height: 300px;
  padding-bottom: 40px;
}
.custom-tree-node {
  display: flex;
  align-items: center;
}
.no-data {
  text-align: center;
  padding: 20px;
  color: #909399;
}
.custom-tree-node {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px;
}
</style>
