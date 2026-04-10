<template>
  <el-dialog
    :title="$t('propertyCommunity.edit.title')"
    :visible.sync="visible"
    width="50%"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item :label="$t('propertyCommunity.edit.openCommunity')">
        <el-input v-model="form.name" disabled />
      </el-form-item>

      <el-form-item :label="$t('propertyCommunity.edit.functions')">
        <el-checkbox v-model="form.isAll" @change="handleAllChange">
          {{ $t('propertyCommunity.edit.all') }}
        </el-checkbox>
        <el-checkbox
          v-for="item in menuGroups"
          :key="item.gId"
          v-model="form.groupIds"
          :label="item.gId"
          @change="handleItemChange"
        >
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
import { listMenuGroup, listMenuGroupCommunity, updateMenuGroupCommunity } from '@/api/community/propertyCommunityApi'

export default {
  name: 'EditPropertyCommunity',
  data() {
    return {
      visible: false,
      form: {
        memberId: '',
        menuGroups: [],
        groupIds: [],
        communityId: '',
        name: '',
        isAll: false
      },
      menuGroups: []
    }
  },
  methods: {
    async open(params) {
      this.form = {
        ...params,
        groupIds: [],
        isAll: false
      }
      await this.getMenuGroups()
      await this.getMenuGroupCommunity()
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
      } catch (error) {
        this.$message.error(this.$t('propertyCommunity.fetchError'))
      }
    },
    async getMenuGroupCommunity() {
      try {
        const params = {
          page: 1,
          row: 100,
          communityId: this.form.communityId
        }
        const { data } = await listMenuGroupCommunity(params)
        this.form.groupIds = data.map(item => item.gId)
        this.handleItemChange()
      } catch (error) {
        this.$message.error(this.$t('propertyCommunity.fetchError'))
      }
    },
    handleAllChange(val) {
      this.form.groupIds = []
      if (val) {
        this.form.isAll = true
        this.form.groupIds = this.menuGroups.map(item => item.gId)
      } else {
        this.form.isAll = false
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
        await updateMenuGroupCommunity(this.form)
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
        communityId: '',
        name: '',
        isAll: false
      }
    }
  }
}
</script>