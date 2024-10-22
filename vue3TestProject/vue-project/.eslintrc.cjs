/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution');

module.exports = {
	root: true,
	extends: [
		'plugin:vue/vue3-essential',
		'eslint:recommended',
		'@vue/eslint-config-prettier',
	],
	env: {
		'vue/setup-compiler-macros': true,
	},
	parserOptions: {
		ecmaVersion: '2022',
		sourceType: 'module',
		tsconfigRootDir: __dirname,
	},
	rules: {
		'no-console': process.env.NODE_DEV === 'production' ? 'error' : 'off',
		'no-debugger': process.env.NODE_DEV === 'production' ? 'error' : 'off',
		'prettier/prettier': [
			'error',
			{
				singleQuote: true,
				semi: true,
				useTabs: true,
				tabWidth: 2,
				trailingComma: 'all',
				printWidth: 80,
				bracketSpancing: true,
				arrowParens: 'avoid',
				endOfLine: 'auto',
			},
		],
	},
};
