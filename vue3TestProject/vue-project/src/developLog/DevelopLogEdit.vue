<template>
	<h2 class="pa-5">개발로그 수정</h2>
	<v-divider :thickness="2"></v-divider>
	<v-container fluid>
		<v-row class="align-center">
			<v-col cols="auto">
				<v-icon class="mr-2">mdi-pencil</v-icon>
				<label for="title">글제목</label>
			</v-col>
			<v-col>
				<input v-model="title" type="text" id="title" class="outlined-input" />
			</v-col>
		</v-row>
	</v-container>
	<div v-if="editor">
		<!-- 볼드, 이태릭, 취소선등 에디터 옵션. 아이콘은 Material icon에서 원하는 아이콘을 가져왔다. -->
		<v-container>
			<button
				@click="editor.chain().focus().toggleBold().run()"
				:disabled="!editor.can().chain().focus().toggleBold().run()"
				:class="{ 'is-active': editor.isActive('bold') }"
			>
				<v-icon icon="mdi-format-bold"></v-icon>
			</button>
			<button
				@click="editor.chain().focus().toggleItalic().run()"
				:disabled="!editor.can().chain().focus().toggleItalic().run()"
				:class="{ 'is-active': editor.isActive('italic') }"
			>
				<v-icon icon="mdi-format-italic"></v-icon>
			</button>
			<button
				@click="editor.chain().focus().toggleStrike().run()"
				:disabled="!editor.can().chain().focus().toggleStrike().run()"
				:class="{ 'is-active': editor.isActive('strike') }"
			>
				<v-icon icon="mdi-format-strikethrough"></v-icon>
			</button>
			<button
				@click="editor.chain().focus().toggleHeading({ level: 1 }).run()"
				:class="{ 'is-active': editor.isActive('heading', { level: 1 }) }"
			>
				<v-icon icon="mdi-format-header-1"></v-icon>
			</button>
			<button
				@click="editor.chain().focus().toggleHeading({ level: 2 }).run()"
				:class="{ 'is-active': editor.isActive('heading', { level: 2 }) }"
			>
				<v-icon icon="mdi-format-header-2"></v-icon>
			</button>
			<button
				@click="editor.chain().focus().toggleHeading({ level: 3 }).run()"
				:class="{ 'is-active': editor.isActive('heading', { level: 3 }) }"
			>
				<v-icon icon="mdi-format-header-3"></v-icon>
			</button>
			<button
				@click="editor.chain().focus().undo().run()"
				:disabled="!editor.can().chain().focus().undo().run()"
			>
				<v-icon icon="mdi-undo"></v-icon>
			</button>
			<button
				@click="editor.chain().focus().redo().run()"
				:disabled="!editor.can().chain().focus().redo().run()"
			>
				<v-icon icon="mdi-redo"></v-icon>
			</button>

			<button @click="addImage">
				<v-icon icon="mdi-image"></v-icon>
			</button>
		</v-container>
	</div>
	<v-container> <editor-content :editor="editor" class="editor" /></v-container>
	<v-container>
		<v-row>
			<v-col>
				<v-row>
					<v-col>
						<v-sheet class="d-flex mb-6">
							<v-sheet class="ma-2 pa-2 me-auto"></v-sheet>
							<v-sheet class="ma-1 pa-1">
								<v-btn color="primary" @click="gobackDetailPage">취소</v-btn>
							</v-sheet>
							<v-sheet class="ma-1 pa-1">
								<v-btn color="success" @click="updateData">저장</v-btn>
							</v-sheet>
						</v-sheet>
					</v-col>
				</v-row>
			</v-col>
		</v-row>
	</v-container>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { onMounted, ref } from 'vue';
import StarterKit from '@tiptap/starter-kit';
import Image from '@tiptap/extension-image';
import ImageResize from 'tiptap-extension-resize-image';
import { useEditor, EditorContent } from '@tiptap/vue-3';

const router = useRouter();
const route = useRoute();
const title = ref('');
const content = ref('');
const today = new Date();
const coverImage = ref(''); // 현재 커버 이미지 URL

// Tiptap 에디터 초기화
const editor = useEditor({
	extensions: [StarterKit, Image, ImageResize],
	content: '',
});

const fetchDevLogData = async () => {
	const id = router.currentRoute.value.params.id;

	try {
		const response = await axios.get(
			`http://localhost:8080/devlogDetail/${id}`,
		);
		const devlog = response.data;

		title.value = devlog.title;
		if (editor.value) {
			editor.value.commands.setContent(devlog.content);
		} else {
			console.error('Editor is not initialized');
		}
	} catch (error) {
		console.error('Error fetching data:', error);
	}
};

// 이미지 추가 함수
const addImage = async () => {
	const input = document.createElement('input');
	input.type = 'file';
	input.accept = 'image/*';
	input.onchange = async event => {
		const file = event.target.files[0];
		if (file) {
			const formData = new FormData();
			formData.append('image', file);
			try {
				// 서버에 이미지 업로드
				console.log(coverImage.value);

				const response = await axios.post(
					'http://localhost:8080/uploadDevlogImage',
					formData,
				);
				const imageUrl = `http://localhost:8080/images/devlog/${response.data.filename}`; // 서버 URL 사용
				console.log('Image URL:', imageUrl); // 이미지 URL 확인
				if (imageUrl) {
					editor.value
						.chain()
						.focus()
						.insertContent(`<img src="${imageUrl}" alt="Image" />`)
						.run();
					content.value = editor.value.getHTML();
				}
			} catch (error) {
				console.error('Error uploading image:', error);
			}
		}
	};
	input.click();
};

const formatDateToYYYYMMDD = date => {
	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');

	return `${year}${month}${day}`;
};

function extractCoverImage(content) {
	const imgRegex = /<img[^>]+src="([^">]+)"/g; // <img> 태그에서 src 속성 추출
	const firstUrl = imgRegex.exec(content); // 첫 번째 이미지URL을 찾음
	return firstUrl && firstUrl[1] ? firstUrl[1] : ''; // URL 반환
}

const updateData = async () => {
	if (!title.value || !editor.value.getJSON().content.length) {
		// 제목이나 내용이 없으면 경고
		alert('제목과 내용을 모두 입력해야 합니다.');
		return;
	}

	const contentHtml = editor.value.getHTML();
	console.log('Content HTML:', contentHtml); // Content 확인
	const coverImageHtml = extractCoverImage(contentHtml);
	console.log('coverImageHtml:', coverImageHtml);

	const devlogData = {
		title: title.value,
		content: contentHtml, // Tiptap 에디터의 HTML 내용 가져오기
		update_dt: formattedDate,
		update_user: 'admin',
		coverImage: coverImageHtml,
	};
	try {
		console.log('coverImage : ', devlogData.coverImage);
		const id = route.params.id;
		const response = await axios.put(
			`http://localhost:8080/updateDevLogEdit/${id}`,
			devlogData,
		);
		console.log('Saved successfully:', response.data);
		// 저장 성공 후 다른 페이지로 이동할 수 있습니다.
		alert('수정되었습니다.');
		gobackDetailPage();
	} catch (error) {
		console.error('Error saving data:', error);
		alert('데이터 저장 중 오류가 발생했습니다.');
	}
};

const formattedDate = formatDateToYYYYMMDD(today);

const gobackDetailPage = () => {
	router.push({ name: 'DevDetail', params: { id: route.params.id } });
};

onMounted(() => {
	fetchDevLogData();
});
</script>

<style scoped>
.outlined-input {
	width: 100%;
	border: 1px solid;
	border-radius: 4px;
	padding: 8px;
	outline: none;
	transition: border-color 0.3s;
}
.editor {
	border: 1px solid #ccc; /* 에디터 테두리 */
	padding: 10px;
	min-height: 300px; /* 에디터 최소 높이 */
	width: auto;
	margin-top: 10px; /* 에디터와 위쪽 요소 간 간격 */
}
</style>
