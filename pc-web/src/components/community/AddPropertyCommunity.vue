<template>
  <el-dialog :title="$t('propertyCommunity.add.title')" :visible.sync="visible" width="50%" @close="handleClose">
    <el-form ref="form" :model="form" label-width="120px" class="text-left">
      <el-form-item :label="$t('propertyCommunity.add.openCommunity')">
        <el-select v-model="form.communityIds" multiple filterable style="width: 100%"
          :placeholder="$t('propertyCommunity.add.selectCommunity')">
          <el-option v-for="item in communitys" :key="item.communityId" :label="item.name" :value="item.communityId" />
        </el-select>
      </el-form-item>

      <el-form-item v-if="form.communityIds && form.communityIds.length > 0"
        :label="$t('propertyCommunity.add.functions')">
        <el-checkbox v-model="form.isAll" @change="handleAllChange">
          {{ $t('propertyCommunity.add.all') }}
        </el-checkbox>
        <el-checkbox v-for="item in menuGroups" :key="item.gId" v-model="form.groupIds" :label="item.gId"
          @change="handleItemChange">
          {{ item.name }}
        </el-checkbox>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="visible = false">
        {{ $t('common.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleSubmit">
        {{ $t('common.save') }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { listNoEnterCommunity, listMenuGroup, joinCommunity } from '@/api/community/propertyCommunityApi'

export default {
  name: 'AddPropertyCommunity',
  data() {
    return {
      visible: false,
      form: {
        memberId: '',
        menuGroups: [],
        groupIds: [],
        communitys: [],
        communityIds: [],
        isAll: true
      },
      menuGroups: [],
      communitys: []
    }
  },
  methods: {
    open(params) {
      this.form.memberId = params.storeId
      this.getMenuGroups()
      this.getNoEnterCommunitys()
      this.visible = true
    },
    async getMenuGroups() {
      try {
        const params = {
          page: 1,
          row: 100,
          storeType: '800900000003'
        }
        const { data } = await listMenuGroup(params)
        this.menuGroups = data
        this.form.groupIds = data.map(item => item.gId)
      } catch (error) {
        this.$message.error(this.$t('propertyCommunity.fetchError'))
      }
    },
    async getNoEnterCommunitys() {
      try {
        const params = {
          communityName: ''
        }
        const data = await listNoEnterCommunity(params)
        this.communitys = data
      } catch (error) {
        this.$message.error(this.$t('propertyCommunity.fetchError'))
      }
    },
    handleAllChange(val) {
      if (val) {
        this.form.groupIds = this.menuGroups.map(item => item.gId)
      } else {
        this.form.groupIds = []
      }
    },
    handleItemChange() {
      if (this.form.groupIds.length < this.menuGroups.length) {
        this.form.isAll = false
      } else {
        this.form.isAll = true
      }
    },
    async handleSubmit() {
      try {
        await joinCommunity(this.form)
        this.$message.success(this.$t('common.operationSuccess'))
        this.$emit('success')
        this.visible = false
      } catch (error) {
        this.$message.error(error.message || this.$t('common.saveFailed'))
      }
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.form = {
        memberId: '',
        menuGroups: [],
        groupIds: [],
        communitys: [],
        communityIds: [],
        isAll: true
      }
    }
  }
}
</script>