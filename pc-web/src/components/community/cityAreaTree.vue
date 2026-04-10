<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <el-button-group>
          <el-button v-if="currentCityArea.areaLevel != '303'" size="small" @click="handleAdd">
            {{ $t('common.add') }}
          </el-button>
          <el-button v-if="currentCityArea.areaCode != '0'" size="small" @click="handleEdit">
            {{ $t('common.edit') }}
          </el-button>
          <el-button v-if="currentCityArea.areaCode != '0'" size="small" type="danger" @click="handleDelete">
            {{ $t('common.delete') }}
          </el-button>
        </el-button-group>
      </div>
      <el-tree ref="areaTree" :data="areaTreeData" :props="defaultProps" node-key="id" highlight-current
        @node-click="handleNodeClick" />
    </el-card>
    <add-city-area ref="addCityArea" @refresh="handleRefresh" />
    <edit-city-area ref="editCityArea" @refresh="handleRefresh" />
    <delete-city-area ref="deleteCityArea" @refresh="handleRefresh" />
  </div>
</template>

<script>
import { getAreaTree } from '@/api/community/cityAreaApi'
import AddCityArea from '@/components/community/addCityArea'
import EditCityArea from '@/components/community/editCityArea'
import DeleteCityArea from '@/components/community/deleteCityArea'
export default {
  name: 'CityAreaTree',
  data() {
    return {
      areaTreeData: [],
      currentCityArea: {},
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  created() {
    this.loadAreaTree()
  },
  components: {
    AddCityArea,
    EditCityArea,
    DeleteCityArea
  },
  methods: {
    async loadAreaTree() {
      try {
        this.areaTreeData = []
        const { data } = await getAreaTree()
        this.areaTreeData.push(data)
      } catch (error) {
        this.$message.error(this.$t('common.fetchError'))
      }
    },
    handleNodeClick(data) {
      this.currentCityArea = data
      this.$emit('switchCityAreaTree', {
        id: data.id,
        areaCode: data.areaCode,
        areaLevel: data.areaLevel,
        areaName: data.text
      })
    },
    handleAdd() {
      if (!this.currentCityArea.id) {
        this.$message.warning(this.$t('cityArea.selectAreaFirst'))
        return
      }

      let areaLevel = this.currentCityArea.areaLevel
      if (areaLevel === '101') {
        areaLevel = '202'
      } else if (areaLevel === '202') {
        areaLevel = '303'
      } else if (areaLevel === '303') {
        this.$message.warning(this.$t('cityArea.cannotAddChild'))
        return
      } else {
        areaLevel = '101'
      }

      this.$refs.addCityArea.open({
        parentAreaCode: this.currentCityArea.areaCode,
        areaLevel: areaLevel,
        parentAreaName: this.currentCityArea.text
      })
    },
    handleEdit() {
      if (!this.currentCityArea.id) {
        this.$message.warning(this.$t('cityArea.selectAreaFirst'))
        return
      }
      this.$refs.editCityArea.open(this.currentCityArea)
    },
    handleDelete() {
      if (!this.currentCityArea.id) {
        this.$message.warning(this.$t('cityArea.selectAreaFirst'))
        return
      }
      this.$refs.deleteCityArea.open(this.currentCityArea)
    },
    handleRefresh() {
      this.loadAreaTree()
    }
  }
}
</script>