<template>
    <div class="quillWrapper">
        <slot name="toolbar"></slot>
        <div :id="id" ref="quillContainer"></div>
        <input
                v-if="useCustomImageHandler"
                id="file-upload"
                ref="fileInput"
                type="file"
                :multiple="multiple"
                accept="image/*"
                style="display:none;"
                @change="emitImageInfo($event)"
        />
    </div>
</template>

<script>
    import Quill from "quill";
    import defaultToolbar from "./helpers/default-toolbar";
    import oldApi from "./helpers/old-api";
    import mergeDeep from "./helpers/merge-deep";
    import MarkdownShortcuts from "./helpers/markdown-shortcuts";
    import { compress } from "../../../operation/utils/date";
    export default {
        name: "VueEditor",
        mixins: [oldApi],
        props: {
            id: {
                type: String,
                default: "quill-container"
            },
            placeholder: {
                type: String,
                default: ""
            },
            value: {
                type: String,
                default: ""
            },
            disabled: {
                type: Boolean
            },
            editorToolbar: {
                type: Array,
                default: () => []
            },
            editorOptions: {
                type: Object,
                required: false,
                default: () => ({})
            },
            useCustomImageHandler: {
                type: Boolean,
                default: false
            },
            useMarkdownShortcuts: {
                type: Boolean,
                default: false
            },
            multiple:{
                type: Boolean,
                default: false
            }
        },
        data: () => ({
            quill: null
        }),
        watch: {
            value(val) {
                if (val != this.quill.root.innerHTML && !this.quill.hasFocus()) {
                    this.quill.root.innerHTML = val;
                }
            },
            disabled(status) {
                this.quill.enable(!status);
            }
        },
        mounted() {
            this.registerCustomModules(Quill);
            this.registerPrototypes();
            this.initializeEditor();
        },
        beforeDestroy() {
            this.quill = null;
            delete this.quill;
        },
        methods: {
            initializeEditor() {
                this.setupQuillEditor();
                this.checkForCustomImageHandler();
                this.handleInitialContent();
                this.registerEditorEventListeners();
                this.$emit("ready", this.quill);
            },
            setupQuillEditor() {
                let editorConfig = {
                    debug: false,
                    modules: this.setModules(),
                    theme: "snow",
                    placeholder: this.placeholder ? this.placeholder : "",
                    readOnly: this.disabled ? this.disabled : false
                };
                this.prepareEditorConfig(editorConfig);
                this.quill = new Quill(this.$refs.quillContainer, editorConfig);
            },
            setModules() {
                let modules = {
                    toolbar: this.editorToolbar.length ? this.editorToolbar : defaultToolbar
                };
                if (this.useMarkdownShortcuts) {
                    Quill.register("modules/markdownShortcuts", MarkdownShortcuts, true);
                    modules["markdownShortcuts"] = {};
                }
                return modules;
            },
            prepareEditorConfig(editorConfig) {
                if (
                    Object.keys(this.editorOptions).length > 0 &&
                    this.editorOptions.constructor === Object
                ) {
                    if (
                        this.editorOptions.modules &&
                        typeof this.editorOptions.modules.toolbar !== "undefined"
                    ) {
                        // We don't want to merge default toolbar with provided toolbar.
                        delete editorConfig.modules.toolbar;
                    }
                    mergeDeep(editorConfig, this.editorOptions);
                }
            },
            registerPrototypes() {
                Quill.prototype.getHTML = function() {
                    return this.container.querySelector(".ql-editor").innerHTML;
                };
                Quill.prototype.getWordCount = function() {
                    return this.container.querySelector(".ql-editor").innerText.length;
                };
            },
            registerEditorEventListeners() {
                this.quill.on("text-change", this.handleTextChange);
                this.quill.on("selection-change", this.handleSelectionChange);
                this.listenForEditorEvent("text-change");
                this.listenForEditorEvent("selection-change");
                this.listenForEditorEvent("editor-change");
            },
            listenForEditorEvent(type) {
                this.quill.on(type, (...args) => {
                    this.$emit(type, ...args);
                });
            },
            handleInitialContent() {
                if (this.value) this.quill.root.innerHTML = this.value; // Set initial editor content
            },
            handleSelectionChange(range, oldRange) {
                if (!range && oldRange) this.$emit("blur", this.quill);
                else if (range && !oldRange) this.$emit("focus", this.quill);
            },
            handleTextChange(delta, oldContents) {
                let editorContent =
                    this.quill.getHTML() === "<p><br></p>" ? "" : this.quill.getHTML();
                this.$emit("input", editorContent);
                if (this.useCustomImageHandler)
                    this.handleImageRemoved(delta, oldContents);
            },
            handleImageRemoved(delta, oldContents) {
                const currrentContents = this.quill.getContents();
                const deletedContents = currrentContents.diff(oldContents);
                const operations = deletedContents.ops;
                operations.map(operation => {
                    if (operation.insert && operation.insert.hasOwnProperty("image")) {
                        const { image } = operation.insert;
                        this.$emit("image-removed", image);
                    }
                });
            },
            checkForCustomImageHandler() {
                this.useCustomImageHandler === true ? this.setupCustomImageHandler() : "";
            },
            setupCustomImageHandler() {
                let toolbar = this.quill.getModule("toolbar");
                toolbar.addHandler("image", this.customImageHandler);
            },
            customImageHandler(image, callback) {
                this.$refs.fileInput.click();
            },
            async emitImageInfo($event) {
                let self=this;
                const resetUploader = function() {
                    var uploader = document.getElementById("file-upload");
                    uploader.value = "";
                };
                // let file = $event.target.files[0];
                let file = $event.target.files;
                let Editor = this.quill;
                let range = Editor.getSelection();
                let cursorLocation = range.index;
                let compressFile=await this.compressImg(file)   //图片做压缩处理

                for (let i=0;i<file.length;i++) {
                    //this.$emit("image-added", compressFile[i],file[i].name, Editor, cursorLocation, resetUploader);
                    this.$emit("image-added", file[i], file[i].name, Editor, cursorLocation, resetUploader);
                }

            },
            dataURLtoBlob(dataurl) {
                var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
                    bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
                while (n--) {
                    u8arr[n] = bstr.charCodeAt(n);
                }
                return new Blob([u8arr], { type: mime });
            },
            compressImg(files){
                let self=this;
                let arr=[];
                return new Promise((resolve,reject)=>{
                    for (var i = 0; i < files.length; i++) {
                        var reader = new FileReader();
                        reader.readAsDataURL(files[i]);
                        reader.onloadstart = function () {
                            //用以在上传前加入一些事件或效果，如载入中...的动画效果
                        };
                        reader.onloadend = function (e) {
                            var dataURL = this.result;
                            var img = new Image();
                            img.src = dataURL;
                            img.onload = function () {  //利用canvas对图片进行压缩
                                var canvas = document.createElement('canvas');
                                let  ctx=canvas.getContext('2d');
                                let initSize=img.src.length;
                                let width=img.width;
                                let height=img.height;
                                canvas.width = width;
                                canvas.height = height;
                                ctx.drawImage(img, 0, 0, width, height);
                                let dataURL = canvas.toDataURL("image/jpeg", 0.1);
                                arr.push(self.dataURLtoBlob(dataURL))
                                console.log(arr)
                                resolve(arr)
                                return arr;
                            };
                        };
                    }
                })

            }

        }
    };
</script>

<style src="quill/dist/quill.snow.css"></style>
<style src="./helpers/styles/vue2-editor.scss" lang="scss"></style>