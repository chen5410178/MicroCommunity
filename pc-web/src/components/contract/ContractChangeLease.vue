<template>
  <el-card class="box-card">
    <div slot="header" class="flex justify-between">
      <span>{{ $t('contractChangeLease.title') }}</span>
    </div>
    <el-form label-width="120px">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('contractChangeLease.startTime')" required>
            <el-date-picker v-model="contractChangeLeaseInfo.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
              :placeholder="$t('contractChangeLease.startTimePlaceholder')" style="width: 100%"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('contractChangeLease.endTime')" required>
            <el-date-picker v-model="contractChangeLeaseInfo.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss"
              :placeholder="$t('contractChangeLease.endTimePlaceholder')" style="width: 100%"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </el-card>
</template>

<script>
export default {
  name: 'ContractChangeLease',
  data() {
    return {
      contractChangeLeaseInfo: {
        startTime: '',
        endTime: '',
        planType: '2002'
      }
    }
  },
  watch: {
    contractChangeLeaseInfo: {
      deep: true,
      handler(newVal) {
        this.$emit('changeNotify', newVal)
      }
    }
  },
  methods: {
    clear() {
      this.contractChangeLeaseInfo = {
        startTime: '',
        endTime: '',
        planType: '2002'
      }
    }
  }
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}
</style>