<template>
  <div class="parking-space-tree-container">
    <el-tree ref="tree" :data="treeData" node-key="id" :props="defaultProps" :highlight-current="true"
      :expand-on-click-node="false" @node-click="handleNodeClick">
      <span slot-scope="{ node, data }" class="custom-tree-node">
        <span>
          <i :class="data.icon" style="margin-right: 5px"></i>
          {{ node.label }}
        </span>
      </span>
    </el-tree>
    <div class="tree-tip">
      <i class="el-icon-info"></i>
      {{ $t('parkingSpaceCreateFee.parkingSpaceTreeTip') }}
    </div>
  </div>
</template>

<script>
import { queryParkingAreaAndSectionsHasOwner, listParkingSpaces } from '@/api/fee/parkingSpaceCreateFeeApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'ParkingSpaceTreeHasOwner',
  data() {
    return {
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      },
      communityId: '',
      lastSelected: {},
      isInitialized: false
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadTreeData()
  },
  methods: {
    async loadTreeData() {
      let lastSelected = localStorage.getItem('lastSelectedParkingSpace')
      if (lastSelected) {
        try {
          this.lastSelected = JSON.parse(lastSelected)
        } catch (error) {
          console.error('解析lastSelectedParkingSpace失败:', error)
          localStorage.removeItem('lastSelectedParkingSpace')
        }
      }

      try {
        const response = await queryParkingAreaAndSectionsHasOwner()
        this.buildTreeData(response.data || [])
      } catch (error) {
        console.error('Failed to load parking area tree data:', error)
        this.$message.error(this.$t('parkingSpaceCreateFee.loadTreeError'))
      }
    },
    buildTreeData(list) {
      const treeData = []
      const paMap = {}

      list.forEach(item => {
        if (!paMap[item.paId]) {
          paMap[item.paId] = {
            id: `pa_${item.paId}`,
            paId: item.paId,
            areaNum: item.num,
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
            areaNum: item.num,
            sectionName: item.sectionName || item.sectionNum,
            icon: 'el-icon-map-location',
            text: item.sectionName || item.sectionNum,
            children: []
          })
        }
      })

      this.treeData = treeData
      this.$nextTick(() => {
        if (this.treeData.length === 0) {
          this.$emit('treeEmpty')
          return
        }
        if (this.lastSelected && this.lastSelected.psId) {
          this.expandAndSelectLastParkingSpace()
        } else {
          this.expandAndSelectFirstParkingSpace()
        }
      })
    },
    async expandAndSelectLastParkingSpace() {
      if (!this.lastSelected || !this.lastSelected.paId || !this.lastSelected.psId) {
        this.expandAndSelectFirstParkingSpace()
        return
      }

      const treeRef = this.$refs.tree
      if (!treeRef || !treeRef.store) {
        setTimeout(() => this.expandAndSelectLastParkingSpace(), 100)
        return
      }

      try {
        const paNode = this.treeData.find(item => item.paId === this.lastSelected.paId)
        if (!paNode) {
          this.expandAndSelectFirstParkingSpace()
          return
        }

        let targetNode = paNode
        if (this.lastSelected.sectionId) {
          const sectionNode = paNode.children.find(item => item.sectionId === this.lastSelected.sectionId)
          if (!sectionNode) {
            this.expandAndSelectFirstParkingSpace()
            return
          }
          targetNode = sectionNode
        }

        const treeNode = treeRef.store.nodesMap[targetNode.id]
        if (treeNode) {
          treeNode.expanded = true
        }

        await this.loadParkingSpaces(targetNode, { data: targetNode })

        this.selectSpecificParkingSpace(this.lastSelected.psId, 0)

        this.isInitialized = true
      } catch (error) {
        console.error('展开上次选择的车位失败:', error)
        this.expandAndSelectFirstParkingSpace()
      }
    },
    selectSpecificParkingSpace(psId, retryCount) {
      const treeRef = this.$refs.tree
      if (!treeRef) return

      const psNodeId = `ps_${psId}`
      const psTreeNode = treeRef.store.nodesMap[psNodeId]

      if (psTreeNode) {
        treeRef.setCurrentKey(psNodeId)
        this.$emit('selectParkingSpace', this.getSelectParkingSpaceData(psTreeNode.data))
      } else {
        if (retryCount < 5) {
          setTimeout(() => this.selectSpecificParkingSpace(psId, retryCount + 1), 200)
        } else {
          console.warn('未找到上次选择的车位，回退选择第一个车位:', psId)
          this.expandAndSelectFirstParkingSpace()
        }
      }
    },
    async expandAndSelectFirstParkingSpace() {
      if (!this.treeData.length) return

      const firstPa = this.treeData[0]
      let targetNode = firstPa
      const treeRef = this.$refs.tree
      if (treeRef && treeRef.store) {
        treeRef.store.nodesMap[firstPa.id] && (treeRef.store.nodesMap[firstPa.id].expanded = true)
      }
      if (firstPa.children && firstPa.children.length > 0) {
        targetNode = firstPa.children[0]
        if (treeRef && treeRef.store) {
          treeRef.store.nodesMap[targetNode.id] && (treeRef.store.nodesMap[targetNode.id].expanded = true)
        }
      }
      await this.loadParkingSpaces(targetNode, { data: targetNode })
      this.isInitialized = true
    },
    async handleNodeClick(data, node) {
      if (data.id.startsWith('sec_')) {
        if (node.loading) return
        node.loading = true
        await this.loadParkingSpaces(data, node)
        node.loading = false
        if (!node.expanded) {
          node.expanded = true
        }
      } else if (data.id.startsWith('pa_')) {
        if (data.children && data.children.length === 0) {
          if (node.loading) return
          node.loading = true
          await this.loadParkingSpaces(data, node)
          node.loading = false
          if (!node.expanded) {
            node.expanded = true
          }
        }
      } else if (data.id.startsWith('ps_')) {
        this.$emit('selectParkingSpace', this.getSelectParkingSpaceData(data))
      }
    },
    getSelectParkingSpaceData(data) {
      const selectedData = {
        paId: data.paId,
        sectionId: data.sectionId,
        psId: data.psId,
        psName: data.psName
      }
      localStorage.setItem('lastSelectedParkingSpace', JSON.stringify(selectedData))
      this.lastSelected = selectedData

      return {
        psId: data.psId,
        psName: data.psName,
        paId: data.paId,
        areaNum: data.areaNum,
        sectionId: data.sectionId,
        sectionName: data.sectionName,
        num: data.num,
        ownerId: data.ownerId,
        ownerName: data.ownerName,
        carId: data.carId,
        carNum: data.carNum,
        startTime: data.startTime,
        endTime: data.endTime
      }
    },
    async loadParkingSpaces(parentData, node) {
      try {
        const params = {
          page: 1,
          row: 1000,
          communityId: this.communityId,
          paId: parentData.paId
        }
        if (parentData.sectionId) {
          params.sectionId = parentData.sectionId
        }

        const response = await listParkingSpaces(params)
        const parkingSpaces = response.parkingSpaces || []

        const psNodes = []
        parkingSpaces.forEach(item => {
          if ((item.state !== 'S' && item.state !== 'H') || !item.ownerId || !item.ownerName) {
            return
          }
          const psName = item.sectionName
            ? `${item.areaNum}-${item.sectionName}-${item.num}`
            : `${item.areaNum}-${item.num}`
          psNodes.push({
            id: `ps_${item.psId}`,
            psId: item.psId,
            paId: item.paId,
            areaNum: item.areaNum,
            sectionId: item.sectionId,
            sectionName: item.sectionName,
            num: item.num,
            ownerId: item.ownerId,
            ownerName: item.ownerName,
            carId: item.carId,
            carNum: item.carNum,
            startTime: item.startTime,
            endTime: item.endTime,
            psName,
            icon: 'el-icon-postcard',
            text: `${item.num}(${item.ownerName})`
          })
        })

        this.$set(node.data, 'children', psNodes)

        if (!this.isInitialized && psNodes.length > 0) {
          this.$emit('selectParkingSpace', this.getSelectParkingSpaceData(psNodes[0]))
        }
        return psNodes
      } catch (error) {
        console.error('Failed to load parking spaces:', error)
        this.$message.error(this.$t('parkingSpaceCreateFee.loadParkingSpaceError'))
        return []
      }
    },
    clearLastSelected() {
      localStorage.removeItem('lastSelectedParkingSpace')
      this.lastSelected = {}
    }
  }
}
</script>

<style lang="scss" scoped>
.parking-space-tree-container {
  height: 100%;
  overflow-y: auto;
  padding: 10px;

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    font-size: 14px;
    padding: 5px 0;
  }

  .tree-tip {
    margin-top: 10px;
    padding: 6px 10px;
    font-size: 12px;
    color: #909399;
    background: #f5f7fa;
    border-radius: 4px;
    line-height: 18px;

    i {
      margin-right: 4px;
    }
  }
}
</style>
