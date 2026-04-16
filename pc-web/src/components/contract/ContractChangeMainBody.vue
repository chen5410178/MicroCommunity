<template>
  <el-card class="box-card">
    <div slot="header" class="flex justify-between">
      <span>{{ $t('contractChangeMainBody.title') }}</span>
    </div>
    <el-form label-width="120px">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('contractChangeMainBody.contractName')" required>
            <el-input v-model="contractChangeMainBodyInfo.contractName"
              :placeholder="$t('contractChangeMainBody.contractNamePlaceholder')"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('contractChangeMainBody.partyA')" required>
            <el-input v-model="contractChangeMainBodyInfo.partyA"
              :placeholder="$t('contractChangeMainBody.partyAPlaceholder')"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('contractChangeMainBody.aContacts')" required>
            <el-input v-model="contractChangeMainBodyInfo.aContacts"
              :placeholder="$t('contractChangeMainBody.aContactsPlaceholder')"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('contractChangeMainBody.aLink')" required>
            <el-input v-model="contractChangeMainBodyInfo.aLink"
              :placeholder="$t('contractChangeMainBody.aLinkPlaceholder')"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('contractChangeMainBody.partyB')" required>
            <el-input v-model="contractChangeMainBodyInfo.partyB"
              :placeholder="$t('contractChangeMainBody.partyBPlaceholder')"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('contractChangeMainBody.bContacts')" required>
            <el-input v-model="contractChangeMainBodyInfo.bContacts"
              :placeholder="$t('contractChangeMainBody.bContactsPlaceholder')"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="$t('contractChangeMainBody.bLink')" required>
            <el-input v-model="contractChangeMainBodyInfo.bLink"
              :placeholder="$t('contractChangeMainBody.bLinkPlaceholder')"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </el-card>
</template>

<script>
export default {
  name: 'ContractChangeMainBody',

  data() {
    return {
      contractChangeMainBodyInfo: {
        contractName: '',
        partyA: '',
        aContacts: '',
        aLink: '',
        partyB: '',
        bContacts: '',
        bLink: '',
        planType: '1001'
      }
    }
  },
  watch: {
    contractChangeMainBodyInfo: {
      deep: true,
      handler(newVal) {
        this.$emit('changeMainBody', newVal)
      }
    }
  },
  methods: {
    clear() {
      this.contractChangeMainBodyInfo = {
        contractName: '',
        partyA: '',
        aContacts: '',
        aLink: '',
        partyB: '',
        bContacts: '',
        bLink: '',
        planType: '1001'
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