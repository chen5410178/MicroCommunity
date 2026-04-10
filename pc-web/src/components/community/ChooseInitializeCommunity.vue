<template>
  <el-dialog
    :title="$t('chooseinitializeCommunity.dialog.title')"
    :visible.sync="visible"
    width="50%"
    :before-close="handleClose"
  >
    <div class="modal-body">
      <li style="color: red;">
        {{ $t('chooseinitializeCommunity.dialog.warning') }}，
        {{ $t('chooseinitializeCommunity.dialog.communityInfo', {
          name: communityInfo.name,
          id: communityInfo.communityId
        }) }}
      </li>
      <br />
      <el-input
        v-model="devPassword"
        :placeholder="$t('chooseinitializeCommunity.dialog.passwordPlaceholder')"
        type="password"
        show-password
      />
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">
        {{ $t('chooseinitializeCommunity.button.cancel') }}
      </el-button>
      <el-button type="primary" @click="handleConfirm">
        {{ $t('chooseinitializeCommunity.button.confirm') }}
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'ChooseInitializeCommunity',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    communityInfo: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      devPassword: ''
    }
  },
  methods: {
    handleClose() {
      this.devPassword = ''
      this.$emit('update:visible', false)
    },
    handleConfirm() {
      if (!this.devPassword) {
        this.$message.warning(this.$t('chooseinitializeCommunity.validation.passwordRequired'))
        return
      }
      
      this.$emit('confirm', {
        communityId: this.communityInfo.communityId,
        devPassword: this.devPassword
      })
      this.handleClose()
    }
  }
}
</script>