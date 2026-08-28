<template>
  <div class="fee-type-tree-container">
    <div class="text-left">
      <el-button v-if="feeTypeTreeInfo.feeTypeCd" type="btn" size="mini" @click="_openAddFeeTypeModal">
        {{ $t('feeTypeTree.add') }}
      </el-button>
      <el-button v-if="feeTypeTreeInfo.ftId" type="btn" size="mini" @click="_openEditFeeTypeModel">
        {{ $t('feeTypeTree.edit') }}
      </el-button>
      <el-button v-if="feeTypeTreeInfo.ftId" type="btn" size="mini" @click="_openDeleteFeeTypeModel">
        {{ $t('feeTypeTree.delete') }}
      </el-button>
    </div>

    <div class="tree-content">
      <el-tree 
        v-if="feeTypeTreeInfo.feeTypeCds.length > 0"
        ref="feeTypeTree" 
        :data="feeTypeTreeInfo.feeTypeCds" 
        :props="treeProps" 
        node-key="id" 
        highlight-current
        :expand-on-click-node="false" 
        @node-click="handleNodeClick"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <i :class="data.icon" style="margin-right:5px"></i>
            <span>{{ data.name }}</span>
          </span>
        </template>
      </el-tree>
      <div v-else class="no-data">
        {{ $t('feeTypeTree.noData') }}
      </div>
    </div>

    <!-- 子组件 -->
    <add-fee-type ref="addFeeType" @success="_loadFeeTypeTree"/>
    <edit-fee-type ref="editFeeType" @success="_loadFeeTypeTree"/>
    <delete-fee-type ref="deleteFeeType" @success="_loadFeeTypeTree"/>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryFeeTypeTree, listFeeType } from '@/api/fee/feeConfigManageApi'
import AddFeeType from './AddFeeType'
import EditFeeType from './EditFeeType'
import DeleteFeeType from './DeleteFeeType'

export default {
  name: 'FeeTypeTree',
  components: {
    AddFeeType,
    EditFeeType,
    DeleteFeeType
  },
  data() {
    return {
      feeTypeTreeInfo: {
        feeTypeCds: [],
        callName: '',
        feeTypeCd: '',
        ftId: '',
        curFeeType: {}
      },
      treeProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  mounted() {
    this._initEvent()
    this.feeTypeTreeInfo.callName = this.callName
    this._loadFeeTypeTree()
  },
  methods: {
    _initEvent() {
      this.$on('listFeeTypeTree', this._loadFeeTypeTree)
    },

    async _loadFeeTypeTree() {
      try {
        const params = {
          communityId: getCommunityId()
        }

        const {data} = await queryFeeTypeTree(params)
        console.log(data)

        this.feeTypeTreeInfo.feeTypeCds = this.transformTreeData(data)

        // 默认选中第一个节点
        this.$nextTick(() => {
          if (this.feeTypeTreeInfo.feeTypeCds.length > 0 &&
            this.feeTypeTreeInfo.feeTypeCds[0].children &&
            this.feeTypeTreeInfo.feeTypeCds[0].children.length > 0) {
            const firstNode = this.feeTypeTreeInfo.feeTypeCds[0].children[0]
            this.$refs.feeTypeTree.setCurrentKey(firstNode.id)
            this.handleNodeClick(firstNode)
          }
        })
      } catch (error) {
        console.error('加载费用类型树失败:', error)
        this.$message.error(this.$t('feeTypeTree.loadError'))
      }
    },

    transformTreeData(data) {
      return data.map(item => {
        const node = {
          id: `c_${item.feeTypeCd}`,
          name: item.text,
          feeTypeCd: item.feeTypeCd,
          type: 'category',
          icon: 'el-icon-folder-opened'
        }

        if (item.children && item.children.length > 0) {
          node.children = item.children.map(child => ({
            id: `f_${child.ftId}`,
            name: child.text,
            ftId: child.ftId,
            feeTypeCd: child.feeTypeCd,
            type: 'feeType',
            icon: 'el-icon-document'
          }))
        }

        return node
      })
    },

    handleNodeClick(data) {
      if (data.type === 'category') {
        this.feeTypeTreeInfo.feeTypeCd = data.feeTypeCd
        this.feeTypeTreeInfo.ftId = ''
        this.$emit('selectFeeType', {
          feeTypeCd: data.feeTypeCd,
          ftId: ''
        })
      } else if (data.type === 'feeType') {
        this.feeTypeTreeInfo.feeTypeCd = ''
        this.feeTypeTreeInfo.ftId = data.ftId
        this._loadFeeType(data.ftId)
        this.$emit('selectFeeType', {
          feeTypeCd: data.feeTypeCd,
          ftId: data.ftId
        })
      }
    },

    async _loadFeeType(ftId) {
      try {
        const params = {
          communityId: getCommunityId(),
          ftId: ftId,
          page: 1,
          row: 1
        }

        const { data } = await listFeeType(params)
        if (data && data.length > 0) {
          this.feeTypeTreeInfo.curFeeType = data[0]
        }
      } catch (error) {
        console.error('加载费用类型详情失败:', error)
      }
    },

    _openAddFeeTypeModal() {
      this.$refs.addFeeType.open({
        feeTypeCd: this.feeTypeTreeInfo.feeTypeCd
      })
    },

    _openEditFeeTypeModel() {
      this.$refs.editFeeType.open(this.feeTypeTreeInfo.curFeeType)
    },

    _openDeleteFeeTypeModel() {
      this.$refs.deleteFeeType.open(this.feeTypeTreeInfo.curFeeType)
    }
  },
}
</script>

<style lang="scss" scoped>
.fee-type-tree-container {
  background-color: #fff;
  border-radius: 4px;
  height: 100%;

  .tree-header {
    padding: 10px;
    border-bottom: 1px solid #e6e6e6;
    text-align: center;

    .el-button {
      margin: 0 2px;
    }
  }

  .tree-content {
    height: calc(100% - 50px);
    overflow-y: auto;
    padding: 10px;

    ::v-deep .el-tree {
      background: transparent;

      .el-tree-node {
        margin: 2px 0;

        .el-tree-node__content {
          height: 30px;
          border-radius: 4px;

          &:hover {
            background-color: #f5f7fa;
          }
        }

        &.is-current > .el-tree-node__content {
          background-color: #ecf5ff;
          color: #409eff;
        }
      }
    }
  }
}

.custom-tree-node {
  display: flex;
  align-items: center;
  font-size: 14px;
  padding-right: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 200px;
}

.no-data {
  text-align: center;
  padding: 20px;
  color: #909399;
}
</style>